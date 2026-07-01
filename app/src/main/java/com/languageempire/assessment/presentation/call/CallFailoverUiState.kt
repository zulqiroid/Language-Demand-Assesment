package com.languageempire.assessment.presentation.call

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.languageempire.assessment.presentation.call.model.CallFailureReasonUiModel
import com.languageempire.assessment.presentation.call.model.CallProviderUiModel

sealed interface CallFailoverUiState {

    data object Idle : CallFailoverUiState

    @Immutable
    data class Connecting(
        val provider: CallProviderUiModel
    ) : CallFailoverUiState

    @Immutable
    data class RetryingWithBackupProvider(
        val failedProvider: CallProviderUiModel,
        val backupProvider: CallProviderUiModel,
        val reason: CallFailureReasonUiModel
    ) : CallFailoverUiState

    @Immutable
    data class Connected(
        val provider: CallProviderUiModel
    ) : CallFailoverUiState

    @Immutable
    data class Failed(
        @StringRes val messageRes: Int,
        val primaryFailureReason: CallFailureReasonUiModel,
        val backupFailureReason: CallFailureReasonUiModel
    ) : CallFailoverUiState
}