package com.languageempire.assessment.data.source

import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderScenario
import com.languageempire.assessment.domain.model.CallProviderType
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryCallProviderScenarioDataSource @Inject constructor() : CallProviderScenarioDataSource {

    private val scenarioReference = AtomicReference(
        CallProviderScenarioDefaults.Default
    )

    override fun getBehavior(
        providerType: CallProviderType
    ): CallProviderBehavior {
        val currentScenario = scenarioReference.get()

        return when (providerType) {
            CallProviderType.PROVIDER_A -> currentScenario.providerABehavior
            CallProviderType.PROVIDER_B -> currentScenario.providerBBehavior
        }
    }

    override fun getScenario(): CallProviderScenario {
        return scenarioReference.get()
    }

    override fun updateScenario(
        scenario: CallProviderScenario
    ) {
        scenarioReference.set(scenario)
    }
}