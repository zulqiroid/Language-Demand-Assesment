package com.languageempire.assessment.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

val MaterialTheme.appSpacing: AppSpacing
    @Composable
    @ReadOnlyComposable
    get() = LocalAppSpacing.current

val MaterialTheme.appDimens: AppDimens
    @Composable
    @ReadOnlyComposable
    get() = LocalAppDimens.current