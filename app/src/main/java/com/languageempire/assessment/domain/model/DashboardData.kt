package com.languageempire.assessment.domain.model

data class DashboardData(
    val languageDemands: List<LanguageDemand>,
    val bookingTypeStats: List<BookingTypeStats>
)