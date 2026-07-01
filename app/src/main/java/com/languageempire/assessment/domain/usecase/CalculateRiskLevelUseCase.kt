package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.RiskLevel
import javax.inject.Inject

class CalculateRiskLevelUseCase @Inject constructor() {

    operator fun invoke(
        totalRequests: Int,
        availableInterpreters: Int,
        unassignedBookings: Int,
        averageWaitingTimeMinutes: Int
    ): RiskLevel {
        require(totalRequests >= 0) {
            "Total requests cannot be negative."
        }

        require(availableInterpreters >= 0) {
            "Available interpreters cannot be negative."
        }

        require(unassignedBookings >= 0) {
            "Unassigned bookings cannot be negative."
        }

        require(averageWaitingTimeMinutes >= 0) {
            "Average waiting time cannot be negative."
        }

        require(unassignedBookings <= totalRequests) {
            "Unassigned bookings cannot be greater than total requests."
        }

        val unassignedRatio = calculateUnassignedRatio(
            totalRequests = totalRequests,
            unassignedBookings = unassignedBookings
        )

        return when {
            shouldMarkRed(
                availableInterpreters = availableInterpreters,
                unassignedBookings = unassignedBookings,
                averageWaitingTimeMinutes = averageWaitingTimeMinutes,
                unassignedRatio = unassignedRatio
            ) -> RiskLevel.RED

            shouldMarkAmber(
                unassignedBookings = unassignedBookings,
                averageWaitingTimeMinutes = averageWaitingTimeMinutes,
                unassignedRatio = unassignedRatio
            ) -> RiskLevel.AMBER

            else -> RiskLevel.GREEN
        }
    }

    private fun calculateUnassignedRatio(
        totalRequests: Int,
        unassignedBookings: Int
    ): Double {
        if (totalRequests == 0) {
            return ZERO_RATIO
        }

        return unassignedBookings.toDouble() / totalRequests.toDouble()
    }

    private fun shouldMarkRed(
        availableInterpreters: Int,
        unassignedBookings: Int,
        averageWaitingTimeMinutes: Int,
        unassignedRatio: Double
    ): Boolean {
        return unassignedBookings >= RED_UNASSIGNED_BOOKINGS_THRESHOLD ||
            availableInterpreters <= RED_AVAILABLE_INTERPRETERS_THRESHOLD ||
            averageWaitingTimeMinutes >= RED_WAITING_TIME_MINUTES_THRESHOLD ||
            unassignedRatio >= RED_UNASSIGNED_RATIO_THRESHOLD
    }

    private fun shouldMarkAmber(
        unassignedBookings: Int,
        averageWaitingTimeMinutes: Int,
        unassignedRatio: Double
    ): Boolean {
        return unassignedBookings >= AMBER_UNASSIGNED_BOOKINGS_THRESHOLD ||
            averageWaitingTimeMinutes >= AMBER_WAITING_TIME_MINUTES_THRESHOLD ||
            unassignedRatio >= AMBER_UNASSIGNED_RATIO_THRESHOLD
    }

    private companion object {
        const val ZERO_RATIO = 0.0

        const val RED_UNASSIGNED_BOOKINGS_THRESHOLD = 40
        const val RED_AVAILABLE_INTERPRETERS_THRESHOLD = 10
        const val RED_WAITING_TIME_MINUTES_THRESHOLD = 15
        const val RED_UNASSIGNED_RATIO_THRESHOLD = 0.20

        const val AMBER_UNASSIGNED_BOOKINGS_THRESHOLD = 10
        const val AMBER_WAITING_TIME_MINUTES_THRESHOLD = 8
        const val AMBER_UNASSIGNED_RATIO_THRESHOLD = 0.10
    }
}