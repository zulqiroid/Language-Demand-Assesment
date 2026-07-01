package com.languageempire.assessment.fake

import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderScenario
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.domain.repository.CallProviderScenarioRepository

class FakeCallProviderScenarioRepository(
    initialScenario: CallProviderScenario = CallProviderScenario(
        providerABehavior = CallProviderBehavior.UNAVAILABLE,
        providerBBehavior = CallProviderBehavior.SUCCESS
    )
) : CallProviderScenarioRepository {

    private var currentScenario: CallProviderScenario = initialScenario

    override fun getScenario(): CallProviderScenario {
        return currentScenario
    }

    override fun updateProviderBehavior(
        providerType: CallProviderType,
        behavior: CallProviderBehavior
    ) {
        currentScenario = when (providerType) {
            CallProviderType.PROVIDER_A -> currentScenario.copy(
                providerABehavior = behavior
            )

            CallProviderType.PROVIDER_B -> currentScenario.copy(
                providerBBehavior = behavior
            )
        }
    }
}