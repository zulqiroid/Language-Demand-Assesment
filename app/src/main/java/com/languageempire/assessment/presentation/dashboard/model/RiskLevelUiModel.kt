package com.languageempire.assessment.presentation.dashboard.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.languageempire.assessment.domain.model.RiskLevel

@Immutable
data class RiskLevelUiModel(
    val level: RiskLevel,
    @StringRes val labelRes: Int
)