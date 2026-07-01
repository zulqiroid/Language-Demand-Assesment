package com.languageempire.assessment.data.repository

import com.languageempire.assessment.data.source.CallProviderScenarioDataSource
import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderScenario
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.domain.repository.CallProviderScenarioRepository
import javax.inject.Inject

class CallProviderScenarioRepositoryImpl @Inject constructor(
    private val scenarioDataSource: CallProviderScenarioDataSource
) : CallProviderScenarioRepository {

    override fun getScenario(): CallProviderScenario {
        return scenarioDataSource.getScenario()
    }

    override fun updateProviderBehavior(
        providerType: CallProviderType,
        behavior: CallProviderBehavior
    ) {
        val currentScenario = scenarioDataSource.getScenario()

        val updatedScenario = when (providerType) {
            CallProviderType.PROVIDER_A -> currentScenario.copy(
                providerABehavior = behavior
            )

            CallProviderType.PROVIDER_B -> currentScenario.copy(
                providerBBehavior = behavior
            )
        }

        scenarioDataSource.updateScenario(
            scenario = updatedScenario
        )
    }
}