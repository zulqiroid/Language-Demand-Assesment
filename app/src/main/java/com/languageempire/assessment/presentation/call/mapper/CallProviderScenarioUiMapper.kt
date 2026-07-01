package com.languageempire.assessment.presentation.call.mapper

import com.languageempire.assessment.R
import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderScenario
import com.languageempire.assessment.presentation.call.CallProviderScenarioUiState
import com.languageempire.assessment.presentation.call.model.ProviderBehaviorUiModel
import javax.inject.Inject

class CallProviderScenarioUiMapper @Inject constructor() {

    fun mapScenario(
        scenario: CallProviderScenario
    ): CallProviderScenarioUiState {
        return CallProviderScenarioUiState(
            providerABehavior = scenario.providerABehavior.toUiModel(),
            providerBBehavior = scenario.providerBBehavior.toUiModel(),
            availableBehaviors = CallProviderBehavior.entries.map { behavior ->
                behavior.toUiModel()
            }
        )
    }

    private fun CallProviderBehavior.toUiModel(): ProviderBehaviorUiModel {
        return ProviderBehaviorUiModel(
            behavior = this,
            labelRes = when (this) {
                CallProviderBehavior.SUCCESS -> R.string.provider_behavior_success
                CallProviderBehavior.UNAVAILABLE -> R.string.provider_behavior_unavailable
                CallProviderBehavior.REJECTED -> R.string.provider_behavior_rejected
                CallProviderBehavior.CRASH -> R.string.provider_behavior_crash
                CallProviderBehavior.TIMEOUT -> R.string.provider_behavior_timeout
            }
        )
    }
}