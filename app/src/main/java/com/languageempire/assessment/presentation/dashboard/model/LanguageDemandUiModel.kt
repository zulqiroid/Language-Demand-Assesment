package com.languageempire.assessment.presentation.dashboard.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class LanguageDemandUiModel(
    val id: String,
    val languageName: String,
    @StringRes val bookingTypeLabelRes: Int,
    val totalRequests: Int,
    val availableInterpreters: Int,
    val unassignedBookings: Int,
    val averageWaitingTimeMinutes: Int,
    val riskLevel: RiskLevelUiModel
)