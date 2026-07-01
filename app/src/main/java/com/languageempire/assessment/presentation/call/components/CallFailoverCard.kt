package com.languageempire.assessment.presentation.call.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.R
import com.languageempire.assessment.core.designsystem.component.InfoMetric
import com.languageempire.assessment.core.designsystem.component.InfoMetricData
import com.languageempire.assessment.core.designsystem.theme.AppColor
import com.languageempire.assessment.core.designsystem.theme.appDimens
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.presentation.call.CallFailoverAction
import com.languageempire.assessment.presentation.call.CallFailoverUiState
import com.languageempire.assessment.presentation.call.CallProviderScenarioUiState
import com.languageempire.assessment.presentation.call.model.CallFailureReasonUiModel
import com.languageempire.assessment.presentation.call.model.CallProviderUiModel

@Composable
fun CallFailoverCard(
    uiState: CallFailoverUiState,
    scenarioUiState: CallProviderScenarioUiState,
    onAction: (CallFailoverAction) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    PaddingValues(
                        horizontal = MaterialTheme.appSpacing.large,
                        vertical = MaterialTheme.appSpacing.large
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.large)
        ) {
            CallFailoverHeader()

            ProviderRouteSection(
                uiState = uiState
            )

            ScenarioControlsSection(
                uiState = uiState,
                scenarioUiState = scenarioUiState,
                onAction = onAction
            )

            StatusDetailsSection(
                uiState = uiState
            )

            CallActionRow(
                uiState = uiState,
                onAction = onAction
            )
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
private fun ProviderRouteSection(
    uiState: CallFailoverUiState
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = SECTION_CONTAINER_ALPHA
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.appSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
        ) {
            Text(
                text = stringResource(id = R.string.call_provider_route_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
            ) {
                ProviderNode(
                    modifier = Modifier.weight(1f),
                    providerLabelRes = R.string.provider_a,
                    roleLabelRes = R.string.call_provider_role_primary,
                    visualStatus = uiState.providerAVisualStatus()
                )

                HorizontalDivider(
                    modifier = Modifier.weight(0.35f),
                    thickness = MaterialTheme.appDimens.dividerThickness,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                ProviderNode(
                    modifier = Modifier.weight(1f),
                    providerLabelRes = R.string.provider_b,
                    roleLabelRes = R.string.call_provider_role_backup,
                    visualStatus = uiState.providerBVisualStatus()
                )
            }
        }
    }
}

@Composable
private fun ProviderNode(
    @StringRes providerLabelRes: Int,
    @StringRes roleLabelRes: Int,
    visualStatus: ProviderVisualStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.extraSmall)
    ) {
        Surface(
            modifier = Modifier.size(MaterialTheme.appDimens.iconMedium),
            shape = CircleShape,
            color = visualStatus.indicatorColor()
        ) {}

        Text(
            text = stringResource(id = providerLabelRes),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(id = roleLabelRes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Surface(
            shape = MaterialTheme.shapes.small,
            color = visualStatus.badgeContainerColor(),
            contentColor = visualStatus.badgeContentColor()
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.appSpacing.medium,
                    vertical = MaterialTheme.appSpacing.extraSmall
                ),
                text = stringResource(id = visualStatus.labelRes),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ScenarioControlsSection(
    uiState: CallFailoverUiState,
    scenarioUiState: CallProviderScenarioUiState,
    onAction: (CallFailoverAction) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = SECTION_CONTAINER_ALPHA
        )
    ) {
        CallProviderScenarioControls(
            modifier = Modifier.padding(MaterialTheme.appSpacing.medium),
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
    }
}

@Composable
private fun StatusDetailsSection(
    uiState: CallFailoverUiState
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = SECTION_CONTAINER_ALPHA
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.appSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
        ) {
            Text(
                text = stringResource(id = R.string.call_status_details_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            InfoMetric(
                data = InfoMetricData(
                    labelRes = R.string.call_current_status,
                    value = uiState.statusMessage()
                )
            )

            when (uiState) {
                is CallFailoverUiState.RetryingWithBackupProvider -> {
                    FailureReasonMetric(
                        labelRes = R.string.call_status_reason,
                        reason = uiState.reason
                    )
                }

                is CallFailoverUiState.Failed -> {
                    FailureReasonMetric(
                        labelRes = R.string.call_primary_failure,
                        reason = uiState.primaryFailureReason
                    )

                    FailureReasonMetric(
                        labelRes = R.string.call_backup_failure,
                        reason = uiState.backupFailureReason
                    )
                }

                CallFailoverUiState.Idle,
                is CallFailoverUiState.Connecting,
                is CallFailoverUiState.Connected -> Unit
            }
        }
    }
}

@Composable
private fun FailureReasonMetric(
    @StringRes labelRes: Int,
    reason: CallFailureReasonUiModel
) {
    InfoMetric(
        data = InfoMetricData(
            labelRes = labelRes,
            value = stringResource(id = reason.messageRes)
        )
    )
}

@Composable
private fun CallActionRow(
    uiState: CallFailoverUiState,
    onAction: (CallFailoverAction) -> Unit
) {
    val isInProgress = uiState.isCallInProgress()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = MaterialTheme.appDimens.buttonMinHeight),
            enabled = !isInProgress,
            onClick = {
                when (uiState) {
                    is CallFailoverUiState.Connected -> {
                        onAction(CallFailoverAction.Reset)
                    }

                    CallFailoverUiState.Idle,
                    is CallFailoverUiState.Failed -> {
                        onAction(CallFailoverAction.StartCall)
                    }

                    is CallFailoverUiState.Connecting,
                    is CallFailoverUiState.RetryingWithBackupProvider -> Unit
                }
            }
        ) {
            Text(
                text = stringResource(id = uiState.primaryActionLabelRes())
            )
        }

        if (uiState is CallFailoverUiState.Failed) {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = MaterialTheme.appDimens.buttonMinHeight),
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

@Composable
private fun CallFailoverUiState.statusMessage(): String {
    return when (this) {
        CallFailoverUiState.Idle -> {
            stringResource(id = R.string.call_status_idle)
        }

        is CallFailoverUiState.Connecting -> {
            stringResource(
                id = R.string.call_status_connecting,
                stringResource(id = provider.labelRes)
            )
        }

        is CallFailoverUiState.RetryingWithBackupProvider -> {
            stringResource(
                id = R.string.call_status_retrying,
                stringResource(id = failedProvider.labelRes),
                stringResource(id = backupProvider.labelRes)
            )
        }

        is CallFailoverUiState.Connected -> {
            stringResource(
                id = R.string.call_status_connected,
                stringResource(id = provider.labelRes)
            )
        }

        is CallFailoverUiState.Failed -> {
            stringResource(id = R.string.call_status_failed)
        }
    }
}

@StringRes
private fun CallFailoverUiState.primaryActionLabelRes(): Int {
    return when (this) {
        CallFailoverUiState.Idle -> R.string.call_start
        is CallFailoverUiState.Connecting -> R.string.call_in_progress
        is CallFailoverUiState.RetryingWithBackupProvider -> R.string.call_in_progress
        is CallFailoverUiState.Connected -> R.string.call_reset
        is CallFailoverUiState.Failed -> R.string.call_try_again
    }
}

private fun CallFailoverUiState.isCallInProgress(): Boolean {
    return this is CallFailoverUiState.Connecting ||
            this is CallFailoverUiState.RetryingWithBackupProvider
}

private fun CallFailoverUiState.isScenarioEditingEnabled(): Boolean {
    return !isCallInProgress()
}

private fun CallFailoverUiState.providerAVisualStatus(): ProviderVisualStatus {
    return when (this) {
        CallFailoverUiState.Idle -> ProviderVisualStatus.Waiting

        is CallFailoverUiState.Connecting -> {
            if (provider.type == CallProviderType.PROVIDER_A) {
                ProviderVisualStatus.Active
            } else {
                ProviderVisualStatus.Failed
            }
        }

        is CallFailoverUiState.RetryingWithBackupProvider -> ProviderVisualStatus.Failed

        is CallFailoverUiState.Connected -> {
            if (provider.type == CallProviderType.PROVIDER_A) {
                ProviderVisualStatus.Connected
            } else {
                ProviderVisualStatus.Failed
            }
        }

        is CallFailoverUiState.Failed -> ProviderVisualStatus.Failed
    }
}

private fun CallFailoverUiState.providerBVisualStatus(): ProviderVisualStatus {
    return when (this) {
        CallFailoverUiState.Idle -> ProviderVisualStatus.Waiting

        is CallFailoverUiState.Connecting -> {
            if (provider.type == CallProviderType.PROVIDER_B) {
                ProviderVisualStatus.Active
            } else {
                ProviderVisualStatus.Waiting
            }
        }

        is CallFailoverUiState.RetryingWithBackupProvider -> ProviderVisualStatus.Active

        is CallFailoverUiState.Connected -> {
            if (provider.type == CallProviderType.PROVIDER_B) {
                ProviderVisualStatus.Connected
            } else {
                ProviderVisualStatus.Waiting
            }
        }

        is CallFailoverUiState.Failed -> ProviderVisualStatus.Failed
    }
}

@Immutable
private enum class ProviderVisualStatus(
    @StringRes val labelRes: Int
) {
    Waiting(R.string.call_provider_status_waiting),
    Active(R.string.call_provider_status_active),
    Connected(R.string.call_provider_status_connected),
    Failed(R.string.call_provider_status_failed)
}

@Composable
private fun ProviderVisualStatus.indicatorColor(): Color {
    return when (this) {
        ProviderVisualStatus.Waiting -> MaterialTheme.colorScheme.outline
        ProviderVisualStatus.Active -> MaterialTheme.colorScheme.primary
        ProviderVisualStatus.Connected -> AppColor.RiskGreen
        ProviderVisualStatus.Failed -> MaterialTheme.colorScheme.error
    }
}

@Composable
private fun ProviderVisualStatus.badgeContainerColor(): Color {
    return when (this) {
        ProviderVisualStatus.Waiting -> MaterialTheme.colorScheme.surface
        ProviderVisualStatus.Active -> MaterialTheme.colorScheme.primaryContainer
        ProviderVisualStatus.Connected -> AppColor.RiskGreenContainer
        ProviderVisualStatus.Failed -> MaterialTheme.colorScheme.errorContainer
    }
}

@Composable
private fun ProviderVisualStatus.badgeContentColor(): Color {
    return when (this) {
        ProviderVisualStatus.Waiting -> MaterialTheme.colorScheme.onSurfaceVariant
        ProviderVisualStatus.Active -> MaterialTheme.colorScheme.onPrimaryContainer
        ProviderVisualStatus.Connected -> AppColor.RiskGreen
        ProviderVisualStatus.Failed -> MaterialTheme.colorScheme.onErrorContainer
    }
}

private const val SECTION_CONTAINER_ALPHA = 0.38f