package com.languageempire.assessment.domain.model

sealed interface CallFailureReason {

    data object Timeout : CallFailureReason

    data object ProviderUnavailable : CallFailureReason

    data object ProviderCrashed : CallFailureReason

    data object ConnectionRejected : CallFailureReason

    data class Unknown(
        val technicalMessage: String? = null
    ) : CallFailureReason
}