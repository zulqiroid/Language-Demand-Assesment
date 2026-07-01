package com.languageempire.assessment.domain.repository

import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderScenario
import com.languageempire.assessment.domain.model.CallProviderType

interface CallProviderScenarioRepository {

    fun getScenario(): CallProviderScenario

    fun updateProviderBehavior(
        providerType: CallProviderType,
        behavior: CallProviderBehavior
    )
}