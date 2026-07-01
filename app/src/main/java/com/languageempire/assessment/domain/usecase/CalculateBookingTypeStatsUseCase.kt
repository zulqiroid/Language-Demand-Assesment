package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.BookingType
import com.languageempire.assessment.domain.model.BookingTypeStats
import com.languageempire.assessment.domain.model.LanguageDemand
import com.languageempire.assessment.domain.model.RiskLevel
import javax.inject.Inject

class CalculateBookingTypeStatsUseCase @Inject constructor(
    private val calculateRiskLevelUseCase: CalculateRiskLevelUseCase
) {

    operator fun invoke(
        languageDemand: List<LanguageDemand>
    ): List<BookingTypeStats> {
        return BookingType.entries.map { bookingType ->
            val matchingDemand = languageDemand.filter { demand ->
                demand.bookingType == bookingType
            }

            createStats(
                bookingType = bookingType,
                matchingDemand = matchingDemand
            )
        }
    }

    private fun createStats(
        bookingType: BookingType,
        matchingDemand: List<LanguageDemand>
    ): BookingTypeStats {
        if (matchingDemand.isEmpty()) {
            return BookingTypeStats(
                bookingType = bookingType,
                totalBookings = 0,
                unassignedBookings = 0,
                riskLevel = RiskLevel.GREEN
            )
        }

        val totalBookings = matchingDemand.sumOf { demand ->
            demand.totalRequests
        }

        val unassignedBookings = matchingDemand.sumOf { demand ->
            demand.unassignedBookings
        }

        val availableInterpreters = matchingDemand.sumOf { demand ->
            demand.availableInterpreters
        }

        val averageWaitingTimeMinutes = calculateWeightedAverageWaitingTime(
            matchingDemand = matchingDemand,
            totalBookings = totalBookings
        )

        return BookingTypeStats(
            bookingType = bookingType,
            totalBookings = totalBookings,
            unassignedBookings = unassignedBookings,
            riskLevel = calculateRiskLevelUseCase(
                totalRequests = totalBookings,
                availableInterpreters = availableInterpreters,
                unassignedBookings = unassignedBookings,
                averageWaitingTimeMinutes = averageWaitingTimeMinutes
            )
        )
    }

    private fun calculateWeightedAverageWaitingTime(
        matchingDemand: List<LanguageDemand>,
        totalBookings: Int
    ): Int {
        if (matchingDemand.isEmpty() || totalBookings == 0) {
            return ZERO_WAITING_TIME_MINUTES
        }

        val weightedWaitingTime = matchingDemand.sumOf { demand ->
            demand.averageWaitingTimeMinutes * demand.totalRequests
        }

        return weightedWaitingTime / totalBookings
    }

    private companion object {
        const val ZERO_WAITING_TIME_MINUTES = 0
    }
}