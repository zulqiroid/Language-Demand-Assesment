package com.languageempire.assessment.presentation.call

import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderType

sealed interface CallFailoverAction {

    data object StartCall : CallFailoverAction

    data object Reset : CallFailoverAction

    data class UpdateProviderBehavior(
        val providerType: CallProviderType,
        val behavior: CallProviderBehavior
    ) : CallFailoverAction
}