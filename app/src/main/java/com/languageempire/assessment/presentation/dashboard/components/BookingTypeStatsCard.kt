package com.languageempire.assessment.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.R
import com.languageempire.assessment.core.designsystem.component.InfoMetric
import com.languageempire.assessment.core.designsystem.component.InfoMetricData
import com.languageempire.assessment.core.designsystem.theme.appDimens
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.presentation.dashboard.model.BookingTypeStatsUiModel

@Composable
fun BookingTypeStatsCard(
    model: BookingTypeStatsUiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = MaterialTheme.appDimens.cardMinHeight),
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
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = model.bookingTypeLabelRes),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                RiskBadge(
                    riskLevel = model.riskLevel
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.large)
            ) {
                InfoMetric(
                    modifier = Modifier.weight(1f),
                    data = InfoMetricData(
                        labelRes = R.string.label_total_bookings,
                        value = model.totalBookings.toString()
                    )
                )

                InfoMetric(
                    modifier = Modifier.weight(1f),
                    data = InfoMetricData(
                        labelRes = R.string.label_unassigned_bookings,
                        value = model.unassignedBookings.toString()
                    )
                )
            }
        }
    }
}