package com.languageempire.assessment.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimens(
    val cardMinHeight: Dp = 96.dp,
    val riskBadgeMinWidth: Dp = 72.dp,
    val buttonMinHeight: Dp = 48.dp,
    val iconSmall: Dp = 18.dp,
    val iconMedium: Dp = 24.dp,
    val dividerThickness: Dp = 1.dp,
    val screenMaxWidth: Dp = 900.dp
)

internal val LocalAppDimens = staticCompositionLocalOf {
    AppDimens()
}