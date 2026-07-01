package com.languageempire.assessment.presentation.dashboard.model

import androidx.compose.runtime.Immutable

@Immutable
data class DashboardSummaryUiModel(
    val totalRequests: Int,
    val totalAvailableInterpreters: Int,
    val totalUnassignedBookings: Int,
    val averageWaitingTimeMinutes: Int,
    val redRiskLanguagesCount: Int
)