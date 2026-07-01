package com.languageempire.assessment.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.R
import com.languageempire.assessment.core.designsystem.component.InfoMetric
import com.languageempire.assessment.core.designsystem.component.InfoMetricData
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.presentation.dashboard.model.DashboardSummaryUiModel

@Composable
fun DashboardSummaryCard(
    summary: DashboardSummaryUiModel,
    modifier: Modifier = Modifier
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.large)
            ) {
                InfoMetric(
                    modifier = Modifier.weight(1f),
                    data = InfoMetricData(
                        labelRes = R.string.label_total_requests,
                        value = summary.totalRequests.toString()
                    )
                )

                InfoMetric(
                    modifier = Modifier.weight(1f),
                    data = InfoMetricData(
                        labelRes = R.string.label_available_interpreters,
                        value = summary.totalAvailableInterpreters.toString()
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.large)
            ) {
                InfoMetric(
                    modifier = Modifier.weight(1f),
                    data = InfoMetricData(
                        labelRes = R.string.label_unassigned_bookings,
                        value = summary.totalUnassignedBookings.toString()
                    )
                )

                InfoMetric(
                    modifier = Modifier.weight(1f),
                    data = InfoMetricData(
                        labelRes = R.string.label_average_waiting_time,
                        value = stringResource(
                            id = R.string.minutes_short_format,
                            summary.averageWaitingTimeMinutes
                        )
                    )
                )
            }

            InfoMetric(
                data = InfoMetricData(
                    labelRes = R.string.label_red_risk_languages,
                    value = summary.redRiskLanguagesCount.toString()
                )
            )
        }
    }
}