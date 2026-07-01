package com.languageempire.assessment.data.source

import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderScenario

internal object CallProviderScenarioDefaults {

    val Default = CallProviderScenario(
        providerABehavior = CallProviderBehavior.UNAVAILABLE,
        providerBBehavior = CallProviderBehavior.SUCCESS
    )
}