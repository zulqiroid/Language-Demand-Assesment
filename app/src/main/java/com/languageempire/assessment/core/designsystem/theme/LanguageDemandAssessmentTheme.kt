package com.languageempire.assessment.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = AppColor.Primary,
    onPrimary = AppColor.OnPrimary,
    primaryContainer = AppColor.PrimaryContainer,
    onPrimaryContainer = AppColor.OnPrimaryContainer,
    secondary = AppColor.Secondary,
    onSecondary = AppColor.OnSecondary,
    secondaryContainer = AppColor.SecondaryContainer,
    onSecondaryContainer = AppColor.OnSecondaryContainer,
    background = AppColor.BackgroundLight,
    onBackground = AppColor.OnBackgroundLight,
    surface = AppColor.SurfaceLight,
    onSurface = AppColor.OnSurfaceLight,
    surfaceVariant = AppColor.SurfaceVariantLight,
    onSurfaceVariant = AppColor.OnSurfaceVariantLight,
    error = AppColor.Error,
    onError = AppColor.OnError,
    errorContainer = AppColor.ErrorContainer,
    onErrorContainer = AppColor.OnErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColor.PrimaryContainer,
    onPrimary = AppColor.OnPrimaryContainer,
    primaryContainer = AppColor.Primary,
    onPrimaryContainer = AppColor.OnPrimary,
    secondary = AppColor.SecondaryContainer,
    onSecondary = AppColor.OnSecondaryContainer,
    secondaryContainer = AppColor.Secondary,
    onSecondaryContainer = AppColor.OnSecondary,
    background = AppColor.BackgroundDark,
    onBackground = AppColor.OnBackgroundDark,
    surface = AppColor.SurfaceDark,
    onSurface = AppColor.OnSurfaceDark,
    surfaceVariant = AppColor.SurfaceVariantDark,
    onSurfaceVariant = AppColor.OnSurfaceVariantDark,
    error = AppColor.ErrorContainer,
    onError = AppColor.OnErrorContainer,
    errorContainer = AppColor.Error,
    onErrorContainer = AppColor.OnError
)

@Composable
fun LanguageDemandAssessmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    spacing: AppSpacing = AppSpacing(),
    dimens: AppDimens = AppDimens(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    CompositionLocalProvider(
        LocalAppSpacing provides spacing,
        LocalAppDimens provides dimens
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}