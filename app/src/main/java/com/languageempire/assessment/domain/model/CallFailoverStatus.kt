package com.languageempire.assessment.domain.model

sealed interface CallFailoverStatus {

    data class Connecting(
        val providerType: CallProviderType
    ) : CallFailoverStatus

    data class RetryingWithBackupProvider(
        val failedProvider: CallProviderType,
        val backupProvider: CallProviderType,
        val reason: CallFailureReason
    ) : CallFailoverStatus

    data class Connected(
        val providerType: CallProviderType
    ) : CallFailoverStatus

    data class Failed(
        val primaryFailureReason: CallFailureReason,
        val backupFailureReason: CallFailureReason
    ) : CallFailoverStatus
}