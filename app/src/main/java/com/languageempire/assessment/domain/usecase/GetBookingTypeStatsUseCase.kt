package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.BookingTypeStats
import javax.inject.Inject

class GetBookingTypeStatsUseCase @Inject constructor(
    private val getLanguageDemandUseCase: GetLanguageDemandUseCase,
    private val calculateBookingTypeStatsUseCase: CalculateBookingTypeStatsUseCase
) {

    suspend operator fun invoke(): List<BookingTypeStats> {
        val languageDemand = getLanguageDemandUseCase()

        return calculateBookingTypeStatsUseCase(
            languageDemand = languageDemand
        )
    }
}