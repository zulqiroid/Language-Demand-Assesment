package com.languageempire.assessment.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimens(
    val screenMaxWidth: Dp = 960.dp,
    val wideScreenBreakpoint: Dp = 600.dp,
    val stateCardMaxWidth: Dp = 440.dp,
    val cardMinHeight: Dp = 96.dp,
    val heroCardMinHeight: Dp = 148.dp,
    val riskBadgeMinWidth: Dp = 72.dp,
    val buttonMinHeight: Dp = 48.dp,
    val iconSmall: Dp = 18.dp,
    val iconMedium: Dp = 24.dp,
    val dividerThickness: Dp = 1.dp
)

internal val LocalAppDimens = staticCompositionLocalOf {
    AppDimens()
}