package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.DashboardData
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val getLanguageDemandUseCase: GetLanguageDemandUseCase,
    private val calculateBookingTypeStatsUseCase: CalculateBookingTypeStatsUseCase
) {

    suspend operator fun invoke(): DashboardData {
        val languageDemands = getLanguageDemandUseCase()

        val bookingTypeStats = calculateBookingTypeStatsUseCase(
            languageDemand = languageDemands
        )

        return DashboardData(
            languageDemands = languageDemands,
            bookingTypeStats = bookingTypeStats
        )
    }
}