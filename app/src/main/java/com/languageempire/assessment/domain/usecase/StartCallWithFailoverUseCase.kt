package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.core.coroutine.DispatcherProvider
import com.languageempire.assessment.core.time.TimeProvider
import com.languageempire.assessment.domain.model.CallConnectionResult
import com.languageempire.assessment.domain.model.CallFailoverEvent
import com.languageempire.assessment.domain.model.CallFailoverStatus
import com.languageempire.assessment.domain.model.CallFailureReason
import com.languageempire.assessment.domain.provider.BackupCallProvider
import com.languageempire.assessment.domain.provider.CallProvider
import com.languageempire.assessment.domain.provider.PrimaryCallProvider
import com.languageempire.assessment.domain.repository.FailoverLogger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class StartCallWithFailoverUseCase @Inject constructor(
    @PrimaryCallProvider private val primaryProvider: CallProvider,
    @BackupCallProvider private val backupProvider: CallProvider,
    private val failoverLogger: FailoverLogger,
    private val timeProvider: TimeProvider,
    private val dispatcherProvider: DispatcherProvider
) {

    operator fun invoke(): Flow<CallFailoverStatus> {
        return flow {
            validateProviders()

            emit(
                CallFailoverStatus.Connecting(
                    providerType = primaryProvider.providerType
                )
            )

            val primaryResult = connectSafely(
                provider = primaryProvider
            )

            when (primaryResult) {
                is CallConnectionResult.Connected -> {
                    emit(
                        CallFailoverStatus.Connected(
                            providerType = primaryResult.providerType
                        )
                    )
                }

                is CallConnectionResult.Failed -> {
                    logFailoverEventSafely(
                        primaryFailure = primaryResult
                    )

                    emit(
                        CallFailoverStatus.RetryingWithBackupProvider(
                            failedProvider = primaryResult.providerType,
                            backupProvider = backupProvider.providerType,
                            reason = primaryResult.reason
                        )
                    )

                    emit(
                        CallFailoverStatus.Connecting(
                            providerType = backupProvider.providerType
                        )
                    )

                    val backupResult = connectSafely(
                        provider = backupProvider
                    )

                    when (backupResult) {
                        is CallConnectionResult.Connected -> {
                            emit(
                                CallFailoverStatus.Connected(
                                    providerType = backupResult.providerType
                                )
                            )
                        }

                        is CallConnectionResult.Failed -> {
                            emit(
                                CallFailoverStatus.Failed(
                                    primaryFailureReason = primaryResult.reason,
                                    backupFailureReason = backupResult.reason
                                )
                            )
                        }
                    }
                }
            }
        }.flowOn(dispatcherProvider.io)
    }

    private fun validateProviders() {
        require(primaryProvider.providerType != backupProvider.providerType) {
            "Primary and backup providers must be different."
        }
    }

    private suspend fun connectSafely(
        provider: CallProvider
    ): CallConnectionResult {
        return try {
            withTimeout(PROVIDER_TIMEOUT_MILLIS) {
                provider.connect()
            }
        } catch (exception: TimeoutCancellationException) {
            CallConnectionResult.Failed(
                providerType = provider.providerType,
                reason = CallFailureReason.Timeout
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Throwable) {
            CallConnectionResult.Failed(
                providerType = provider.providerType,
                reason = CallFailureReason.ProviderCrashed
            )
        }
    }

    private suspend fun logFailoverEventSafely(
        primaryFailure: CallConnectionResult.Failed
    ) {
        try {
            failoverLogger.log(
                event = CallFailoverEvent(
                    failedProvider = primaryFailure.providerType,
                    backupProvider = backupProvider.providerType,
                    failureReason = primaryFailure.reason,
                    timestampMillis = timeProvider.currentTimeMillis()
                )
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Throwable) {
            // Logging is non-critical for call continuity.
            // A logging failure must not block the backup provider attempt.
        }
    }

    private companion object {
        const val PROVIDER_TIMEOUT_MILLIS = 3_000L
    }
}