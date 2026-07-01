package com.languageempire.assessment.presentation.dashboard.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.core.designsystem.theme.AppColor
import com.languageempire.assessment.core.designsystem.theme.appDimens
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.domain.model.RiskLevel
import com.languageempire.assessment.presentation.dashboard.model.RiskLevelUiModel

@Composable
fun RiskBadge(
    riskLevel: RiskLevelUiModel,
    modifier: Modifier = Modifier
) {
    val colors = riskLevel.level.toBadgeColors()

    Surface(
        modifier = modifier.defaultMinSize(
            minWidth = MaterialTheme.appDimens.riskBadgeMinWidth
        ),
        shape = MaterialTheme.shapes.small,
        color = colors.containerColor,
        contentColor = colors.contentColor
    ) {
        Text(
            modifier = Modifier.padding(
                PaddingValues(
                    horizontal = MaterialTheme.appSpacing.medium,
                    vertical = MaterialTheme.appSpacing.extraSmall
                )
            ),
            text = stringResource(id = riskLevel.labelRes),
            style = MaterialTheme.typography.labelMedium,
            color = colors.contentColor
        )
    }
}

@Composable
private fun RiskLevel.toBadgeColors(): RiskBadgeColors {
    return when (this) {
        RiskLevel.GREEN -> RiskBadgeColors(
            contentColor = AppColor.RiskGreen,
            containerColor = AppColor.RiskGreenContainer
        )

        RiskLevel.AMBER -> RiskBadgeColors(
            contentColor = AppColor.RiskAmber,
            containerColor = AppColor.RiskAmberContainer
        )

        RiskLevel.RED -> RiskBadgeColors(
            contentColor = AppColor.RiskRed,
            containerColor = AppColor.RiskRedContainer
        )
    }
}

@Immutable
private data class RiskBadgeColors(
    val contentColor: Color,
    val containerColor: Color
)