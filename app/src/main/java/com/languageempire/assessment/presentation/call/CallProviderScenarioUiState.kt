package com.languageempire.assessment.presentation.call

import androidx.compose.runtime.Immutable
import com.languageempire.assessment.presentation.call.model.ProviderBehaviorUiModel

@Immutable
data class CallProviderScenarioUiState(
    val providerABehavior: ProviderBehaviorUiModel,
    val providerBBehavior: ProviderBehaviorUiModel,
    val availableBehaviors: List<ProviderBehaviorUiModel>
)