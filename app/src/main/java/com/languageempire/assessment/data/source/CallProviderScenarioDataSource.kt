package com.languageempire.assessment.data.source

import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderScenario
import com.languageempire.assessment.domain.model.CallProviderType

interface CallProviderScenarioDataSource {

    fun getBehavior(
        providerType: CallProviderType
    ): CallProviderBehavior

    fun getScenario(): CallProviderScenario

    fun updateScenario(
        scenario: CallProviderScenario
    )
}