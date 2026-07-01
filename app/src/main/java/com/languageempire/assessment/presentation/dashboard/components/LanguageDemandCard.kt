package com.languageempire.assessment.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.R
import com.languageempire.assessment.core.designsystem.component.InfoMetric
import com.languageempire.assessment.core.designsystem.component.InfoMetricData
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.presentation.dashboard.model.LanguageDemandUiModel

@Composable
fun LanguageDemandCard(
    model: LanguageDemandUiModel,
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
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
        ) {
            LanguageDemandHeader(
                model = model
            )

            PressureIndicator(
                progress = calculatePressureProgress(
                    unassigned = model.unassignedBookings,
                    total = model.totalRequests
                ),
                riskLevel = model.riskLevel
            )

            LanguageDemandMetrics(
                model = model
            )
        }
    }
}

@Composable
private fun LanguageDemandHeader(
    model: LanguageDemandUiModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.extraSmall)
        ) {
            Text(
                text = model.languageName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(id = model.bookingTypeLabelRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        RiskBadge(
            riskLevel = model.riskLevel
        )
    }
}

@Composable
private fun LanguageDemandMetrics(
    model: LanguageDemandUiModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
        ) {
            MetricTile(
                modifier = Modifier.weight(1f),
                data = InfoMetricData(
                    labelRes = R.string.label_total_requests,
                    value = model.totalRequests.toString()
                )
            )

            MetricTile(
                modifier = Modifier.weight(1f),
                data = InfoMetricData(
                    labelRes = R.string.label_available_interpreters,
                    value = model.availableInterpreters.toString()
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
        ) {
            MetricTile(
                modifier = Modifier.weight(1f),
                data = InfoMetricData(
                    labelRes = R.string.label_unassigned_bookings,
                    value = model.unassignedBookings.toString()
                )
            )

            MetricTile(
                modifier = Modifier.weight(1f),
                data = InfoMetricData(
                    labelRes = R.string.label_average_waiting_time,
                    value = stringResource(
                        id = R.string.minutes_short_format,
                        model.averageWaitingTimeMinutes
                    )
                )
            )
        }
    }
}

@Composable
private fun MetricTile(
    data: InfoMetricData,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = METRIC_TILE_CONTAINER_ALPHA
        )
    ) {
        InfoMetric(
            modifier = Modifier.padding(MaterialTheme.appSpacing.medium),
            data = data
        )
    }
}

private fun calculatePressureProgress(
    unassigned: Int,
    total: Int
): Float {
    if (total <= 0) {
        return ZERO_PROGRESS
    }

    return unassigned.toFloat() / total.toFloat()
}

private const val ZERO_PROGRESS = 0f
private const val METRIC_TILE_CONTAINER_ALPHA = 0.45f