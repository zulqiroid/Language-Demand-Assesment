 package com.languageempire.assessment.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.R
import com.languageempire.assessment.core.designsystem.theme.AppColor
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.domain.model.RiskLevel
import com.languageempire.assessment.presentation.dashboard.model.RiskLevelUiModel
import kotlin.math.roundToInt

@Composable
fun PressureIndicator(
    progress: Float,
    riskLevel: RiskLevelUiModel,
    modifier: Modifier = Modifier
) {
    val safeProgress = progress.coerceIn(
        minimumValue = 0f,
        maximumValue = 1f
    )

    val percentage = (safeProgress * PERCENTAGE_MULTIPLIER).roundToInt()
    val progressColor = riskLevel.toProgressColor()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.extraSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.label_unassigned_pressure),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = stringResource(
                    id = R.string.percentage_format,
                    percentage
                ),
                style = MaterialTheme.typography.labelMedium,
                color = progressColor
            )
        }

        LinearProgressIndicator(
            progress = {
                safeProgress
            },
            modifier = Modifier.fillMaxWidth(),
            color = progressColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun RiskLevelUiModel.toProgressColor(): Color {
    return when (level) {
        RiskLevel.GREEN -> AppColor.RiskGreen
        RiskLevel.AMBER -> AppColor.RiskAmber
        RiskLevel.RED -> AppColor.RiskRed
    }
}

private const val PERCENTAGE_MULTIPLIER = 100