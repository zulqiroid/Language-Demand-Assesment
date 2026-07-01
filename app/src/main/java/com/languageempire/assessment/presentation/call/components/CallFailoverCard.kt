package com.languageempire.assessment.presentation.call.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.R
import com.languageempire.assessment.core.designsystem.component.InfoMetric
import com.languageempire.assessment.core.designsystem.component.InfoMetricData
import com.languageempire.assessment.core.designsystem.theme.appDimens
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.presentation.call.CallFailoverAction
import com.languageempire.assessment.presentation.call.CallFailoverUiState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.presentation.call.CallProviderScenarioUiState

@Composable
fun CallFailoverCard(
    uiState: CallFailoverUiState,
    scenarioUiState: CallProviderScenarioUiState,
    onAction: (CallFailoverAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonState = uiState.toButtonState()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.appSpacing.large),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.large)
        ) {
            CallFailoverHeader()

            CallProviderScenarioControls(
                scenarioUiState = scenarioUiState,
                enabled = uiState.isScenarioEditingEnabled(),
                onBehaviorSelected = { providerType: CallProviderType, behavior: CallProviderBehavior ->
                    onAction(
                        CallFailoverAction.UpdateProviderBehavior(
                            providerType = providerType,
                            behavior = behavior
                        )
                    )
                }
            )

            CallStatusContent(
                uiState = uiState
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = MaterialTheme.appDimens.buttonMinHeight),
                    enabled = buttonState.isEnabled,
                    onClick = {
                        onAction(buttonState.action)
                    }
                ) {
                    Text(
                        text = stringResource(id = buttonState.labelRes)
                    )
                }

                if (uiState !is CallFailoverUiState.Idle) {
                    OutlinedButton(
                        onClick = {
                            onAction(CallFailoverAction.Reset)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.call_reset)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CallFailoverHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.extraSmall)
    ) {
        Text(
            text = stringResource(id = R.string.call_connectivity_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(id = R.string.call_connectivity_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CallStatusContent(
    uiState: CallFailoverUiState
) {
    when (uiState) {
        CallFailoverUiState.Idle -> {
            StatusMessage(
                message = stringResource(id = R.string.call_status_idle)
            )
        }

        is CallFailoverUiState.Connecting -> {
            ProgressStatusMessage(
                message = stringResource(
                    id = R.string.call_status_connecting,
                    stringResource(id = uiState.provider.labelRes)
                )
            )
        }

        is CallFailoverUiState.RetryingWithBackupProvider -> {
            ProgressStatusMessage(
                message = stringResource(
                    id = R.string.call_status_retrying,
                    stringResource(id = uiState.failedProvider.labelRes),
                    stringResource(id = uiState.backupProvider.labelRes)
                )
            )

            InfoMetric(
                data = InfoMetricData(
                    labelRes = R.string.call_primary_failure,
                    value = stringResource(id = uiState.reason.messageRes)
                )
            )
        }

        is CallFailoverUiState.Connected -> {
            StatusMessage(
                message = stringResource(
                    id = R.string.call_status_connected,
                    stringResource(id = uiState.provider.labelRes)
                )
            )
        }

        is CallFailoverUiState.Failed -> {
            StatusMessage(
                message = stringResource(id = uiState.messageRes)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.large)
            ) {
                InfoMetric(
                    modifier = Modifier.weight(1f),
                    data = InfoMetricData(
                        labelRes = R.string.call_primary_failure,
                        value = stringResource(id = uiState.primaryFailureReason.messageRes)
                    )
                )

                InfoMetric(
                    modifier = Modifier.weight(1f),
                    data = InfoMetricData(
                        labelRes = R.string.call_backup_failure,
                        value = stringResource(id = uiState.backupFailureReason.messageRes)
                    )
                )
            }
        }
    }
}

@Composable
private fun StatusMessage(
    message: String
) {
    InfoMetric(
        data = InfoMetricData(
            labelRes = R.string.call_current_status,
            value = message
        )
    )
}

@Composable
private fun ProgressStatusMessage(
    message: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(MaterialTheme.appDimens.iconMedium),
            strokeWidth = MaterialTheme.appSpacing.extraSmall
        )

        InfoMetric(
            modifier = Modifier.weight(1f),
            data = InfoMetricData(
                labelRes = R.string.call_current_status,
                value = message
            )
        )
    }
}

@Composable
private fun CallFailoverUiState.toButtonState(): CallButtonState {
    return when (this) {
        CallFailoverUiState.Idle -> CallButtonState(
            labelRes = R.string.call_start,
            action = CallFailoverAction.StartCall,
            isEnabled = true
        )

        is CallFailoverUiState.Connecting,
        is CallFailoverUiState.RetryingWithBackupProvider -> CallButtonState(
            labelRes = R.string.call_in_progress,
            action = CallFailoverAction.StartCall,
            isEnabled = false
        )

        is CallFailoverUiState.Connected -> CallButtonState(
            labelRes = R.string.call_reset,
            action = CallFailoverAction.Reset,
            isEnabled = true
        )

        is CallFailoverUiState.Failed -> CallButtonState(
            labelRes = R.string.call_try_again,
            action = CallFailoverAction.StartCall,
            isEnabled = true
        )
    }
}

@Immutable
private data class CallButtonState(
    val labelRes: Int,
    val action: CallFailoverAction,
    val isEnabled: Boolean
)

private fun CallFailoverUiState.isScenarioEditingEnabled(): Boolean {
    return this !is CallFailoverUiState.Connecting &&
            this !is CallFailoverUiState.RetryingWithBackupProvider
}