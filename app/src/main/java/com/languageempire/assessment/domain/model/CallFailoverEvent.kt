package com.languageempire.assessment.domain.model

data class CallFailoverEvent(
    val failedProvider: CallProviderType,
    val backupProvider: CallProviderType,
    val failureReason: CallFailureReason,
    val timestampMillis: Long
) {
    init {
        require(timestampMillis > 0L) {
            "Timestamp must be greater than zero."
        }

        require(failedProvider != backupProvider) {
            "Failed provider and backup provider cannot be the same."
        }
    }
}