package com.languageempire.assessment.presentation.call.mapper

import com.languageempire.assessment.R
import com.languageempire.assessment.domain.model.CallFailoverStatus
import com.languageempire.assessment.domain.model.CallFailureReason
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.presentation.call.CallFailoverUiState
import com.languageempire.assessment.presentation.call.model.CallFailureReasonUiModel
import com.languageempire.assessment.presentation.call.model.CallProviderUiModel
import javax.inject.Inject

class CallFailoverUiMapper @Inject constructor() {

    fun mapStatus(
        status: CallFailoverStatus
    ): CallFailoverUiState {
        return when (status) {
            is CallFailoverStatus.Connecting -> {
                CallFailoverUiState.Connecting(
                    provider = status.providerType.toUiModel()
                )
            }

            is CallFailoverStatus.RetryingWithBackupProvider -> {
                CallFailoverUiState.RetryingWithBackupProvider(
                    failedProvider = status.failedProvider.toUiModel(),
                    backupProvider = status.backupProvider.toUiModel(),
                    reason = status.reason.toUiModel()
                )
            }

            is CallFailoverStatus.Connected -> {
                CallFailoverUiState.Connected(
                    provider = status.providerType.toUiModel()
                )
            }

            is CallFailoverStatus.Failed -> {
                CallFailoverUiState.Failed(
                    messageRes = R.string.call_failed_both_providers,
                    primaryFailureReason = status.primaryFailureReason.toUiModel(),
                    backupFailureReason = status.backupFailureReason.toUiModel()
                )
            }
        }
    }

    private fun CallProviderType.toUiModel(): CallProviderUiModel {
        return CallProviderUiModel(
            type = this,
            labelRes = when (this) {
                CallProviderType.PROVIDER_A -> R.string.provider_a
                CallProviderType.PROVIDER_B -> R.string.provider_b
            }
        )
    }

    private fun CallFailureReason.toUiModel(): CallFailureReasonUiModel {
        return CallFailureReasonUiModel(
            reason = this,
            messageRes = when (this) {
                CallFailureReason.Timeout -> R.string.call_failure_timeout
                CallFailureReason.ProviderUnavailable -> R.string.call_failure_unavailable
                CallFailureReason.ProviderCrashed -> R.string.call_failure_crashed
                CallFailureReason.ConnectionRejected -> R.string.call_failure_rejected
                is CallFailureReason.Unknown -> R.string.call_failure_unknown
            }
        )
    }
}