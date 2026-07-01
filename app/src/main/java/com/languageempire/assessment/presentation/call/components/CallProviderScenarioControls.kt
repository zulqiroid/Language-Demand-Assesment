package com.languageempire.assessment.presentation.call.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.R
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.presentation.call.CallProviderScenarioUiState
import com.languageempire.assessment.presentation.call.model.ProviderBehaviorUiModel

@Composable
fun CallProviderScenarioControls(
    scenarioUiState: CallProviderScenarioUiState,
    enabled: Boolean,
    onBehaviorSelected: (CallProviderType, CallProviderBehavior) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
    ) {
        Text(
            text = stringResource(id = R.string.call_scenario_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        ProviderBehaviorSelector(
            labelRes = R.string.call_provider_a_behavior,
            selectedBehavior = scenarioUiState.providerABehavior,
            availableBehaviors = scenarioUiState.availableBehaviors,
            enabled = enabled,
            onBehaviorSelected = { behavior ->
                onBehaviorSelected(
                    CallProviderType.PROVIDER_A,
                    behavior
                )
            }
        )

        ProviderBehaviorSelector(
            labelRes = R.string.call_provider_b_behavior,
            selectedBehavior = scenarioUiState.providerBBehavior,
            availableBehaviors = scenarioUiState.availableBehaviors,
            enabled = enabled,
            onBehaviorSelected = { behavior ->
                onBehaviorSelected(
                    CallProviderType.PROVIDER_B,
                    behavior
                )
            }
        )
    }
}

@Composable
private fun ProviderBehaviorSelector(
    @StringRes labelRes: Int,
    selectedBehavior: ProviderBehaviorUiModel,
    availableBehaviors: List<ProviderBehaviorUiModel>,
    enabled: Boolean,
    onBehaviorSelected: (CallProviderBehavior) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.extraSmall)
    ) {
        Text(
            text = stringResource(id = labelRes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            enabled = enabled,
            onClick = {
                expanded = true
            }
        ) {
            Text(
                text = stringResource(id = selectedBehavior.labelRes)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            availableBehaviors.forEach { behavior ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = behavior.labelRes)
                        )
                    },
                    onClick = {
                        expanded = false
                        onBehaviorSelected(behavior.behavior)
                    }
                )
            }
        }
    }
}