package com.languageempire.assessment.presentation.dashboard.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.languageempire.assessment.domain.model.BookingType

@Immutable
data class BookingTypeStatsUiModel(
    val bookingType: BookingType,
    @StringRes val bookingTypeLabelRes: Int,
    val totalBookings: Int,
    val unassignedBookings: Int,
    val riskLevel: RiskLevelUiModel
)