package com.languageempire.assessment.domain.model

sealed interface CallConnectionResult {

    data class Connected(
        val providerType: CallProviderType
    ) : CallConnectionResult

    data class Failed(
        val providerType: CallProviderType,
        val reason: CallFailureReason
    ) : CallConnectionResult
}