package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.core.coroutine.TestDispatcherProvider
import com.languageempire.assessment.domain.model.CallConnectionResult
import com.languageempire.assessment.domain.model.CallFailoverStatus
import com.languageempire.assessment.domain.model.CallFailureReason
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.fake.FakeCallProvider
import com.languageempire.assessment.fake.FixedTimeProvider
import com.languageempire.assessment.fake.RecordingFailoverLogger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.coroutines.test.TestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class StartCallWithFailoverUseCaseTest {

    @Test
    fun `connects with primary provider when provider A succeeds`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_A
            )
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        )

        val logger = RecordingFailoverLogger()

        val useCase = createUseCase(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider,
            logger = logger,
            testDispatcher = testDispatcher
        )

        val statuses = useCase().toList()

        assertEquals(
            listOf(
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_A
                ),
                CallFailoverStatus.Connected(
                    providerType = CallProviderType.PROVIDER_A
                )
            ),
            statuses
        )

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(0, backupProvider.connectCallCount)
        assertTrue(logger.events.isEmpty())
    }

    @Test
    fun `retries with backup provider when provider A is unavailable`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Failed(
                providerType = CallProviderType.PROVIDER_A,
                reason = CallFailureReason.ProviderUnavailable
            )
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        )

        val logger = RecordingFailoverLogger()

        val useCase = createUseCase(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider,
            logger = logger,
            testDispatcher = testDispatcher
        )

        val statuses = useCase().toList()

        assertEquals(
            listOf(
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_A
                ),
                CallFailoverStatus.RetryingWithBackupProvider(
                    failedProvider = CallProviderType.PROVIDER_A,
                    backupProvider = CallProviderType.PROVIDER_B,
                    reason = CallFailureReason.ProviderUnavailable
                ),
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_B
                ),
                CallFailoverStatus.Connected(
                    providerType = CallProviderType.PROVIDER_B
                )
            ),
            statuses
        )

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(1, backupProvider.connectCallCount)
        assertEquals(1, logger.events.size)

        val loggedEvent = logger.events.first()
        assertEquals(CallProviderType.PROVIDER_A, loggedEvent.failedProvider)
        assertEquals(CallProviderType.PROVIDER_B, loggedEvent.backupProvider)
        assertEquals(CallFailureReason.ProviderUnavailable, loggedEvent.failureReason)
    }

    @Test
    fun `retries with backup provider when provider A crashes`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            throwable = IllegalStateException("Provider A crashed.")
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        )

        val logger = RecordingFailoverLogger()

        val useCase = createUseCase(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider,
            logger = logger,
            testDispatcher = testDispatcher
        )

        val statuses = useCase().toList()

        assertEquals(
            listOf(
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_A
                ),
                CallFailoverStatus.RetryingWithBackupProvider(
                    failedProvider = CallProviderType.PROVIDER_A,
                    backupProvider = CallProviderType.PROVIDER_B,
                    reason = CallFailureReason.ProviderCrashed
                ),
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_B
                ),
                CallFailoverStatus.Connected(
                    providerType = CallProviderType.PROVIDER_B
                )
            ),
            statuses
        )

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(1, backupProvider.connectCallCount)
        assertEquals(1, logger.events.size)
        assertEquals(CallFailureReason.ProviderCrashed, logger.events.first().failureReason)
    }

    @Test
    fun `retries with backup provider when provider A times out`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_A
            ),
            delayMillis = PROVIDER_TIMEOUT_TEST_DELAY_MILLIS
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        )

        val logger = RecordingFailoverLogger()

        val useCase = createUseCase(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider,
            logger = logger,
            testDispatcher = testDispatcher
        )

        val statuses = useCase().toList()

        assertEquals(
            listOf(
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_A
                ),
                CallFailoverStatus.RetryingWithBackupProvider(
                    failedProvider = CallProviderType.PROVIDER_A,
                    backupProvider = CallProviderType.PROVIDER_B,
                    reason = CallFailureReason.Timeout
                ),
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_B
                ),
                CallFailoverStatus.Connected(
                    providerType = CallProviderType.PROVIDER_B
                )
            ),
            statuses
        )

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(1, backupProvider.connectCallCount)
        assertEquals(1, logger.events.size)
        assertEquals(CallFailureReason.Timeout, logger.events.first().failureReason)
    }

    @Test
    fun `emits failed when both providers fail`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Failed(
                providerType = CallProviderType.PROVIDER_A,
                reason = CallFailureReason.ProviderUnavailable
            )
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Failed(
                providerType = CallProviderType.PROVIDER_B,
                reason = CallFailureReason.ConnectionRejected
            )
        )

        val logger = RecordingFailoverLogger()

        val useCase = createUseCase(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider,
            logger = logger,
            testDispatcher = testDispatcher
        )

        val statuses = useCase().toList()

        assertEquals(
            listOf(
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_A
                ),
                CallFailoverStatus.RetryingWithBackupProvider(
                    failedProvider = CallProviderType.PROVIDER_A,
                    backupProvider = CallProviderType.PROVIDER_B,
                    reason = CallFailureReason.ProviderUnavailable
                ),
                CallFailoverStatus.Connecting(
                    providerType = CallProviderType.PROVIDER_B
                ),
                CallFailoverStatus.Failed(
                    primaryFailureReason = CallFailureReason.ProviderUnavailable,
                    backupFailureReason = CallFailureReason.ConnectionRejected
                )
            ),
            statuses
        )

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(1, backupProvider.connectCallCount)
        assertEquals(1, logger.events.size)
    }

    @Test
    fun `continues backup attempt when failover logger fails`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Failed(
                providerType = CallProviderType.PROVIDER_A,
                reason = CallFailureReason.ProviderUnavailable
            )
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        )

        val failingLogger = RecordingFailoverLogger(
            shouldThrow = true
        )

        val useCase = createUseCase(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider,
            logger = failingLogger,
            testDispatcher = testDispatcher
        )

        val statuses = useCase().toList()

        assertEquals(
            CallFailoverStatus.Connected(
                providerType = CallProviderType.PROVIDER_B
            ),
            statuses.last()
        )

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(1, backupProvider.connectCallCount)
    }

    private fun createUseCase(
        primaryProvider: FakeCallProvider,
        backupProvider: FakeCallProvider,
        logger: RecordingFailoverLogger,
        testDispatcher: TestDispatcher
    ): StartCallWithFailoverUseCase {
        return StartCallWithFailoverUseCase(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider,
            failoverLogger = logger,
            timeProvider = FixedTimeProvider(),
            dispatcherProvider = TestDispatcherProvider(
                testDispatcher = testDispatcher
            )
        )
    }

    private companion object {
        const val PROVIDER_TIMEOUT_TEST_DELAY_MILLIS = 5_000L
    }
}