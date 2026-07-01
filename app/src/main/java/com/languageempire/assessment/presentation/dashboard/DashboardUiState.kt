package com.languageempire.assessment.presentation.dashboard

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.languageempire.assessment.presentation.dashboard.model.BookingTypeStatsUiModel
import com.languageempire.assessment.presentation.dashboard.model.DashboardSummaryUiModel
import com.languageempire.assessment.presentation.dashboard.model.LanguageDemandUiModel

sealed interface DashboardUiState {

    data object Loading : DashboardUiState

    @Immutable
    data class Content(
        val summary: DashboardSummaryUiModel,
        val languageDemands: List<LanguageDemandUiModel>,
        val bookingTypeStats: List<BookingTypeStatsUiModel>
    ) : DashboardUiState

    @Immutable
    data class Error(
        @StringRes val messageRes: Int
    ) : DashboardUiState
}