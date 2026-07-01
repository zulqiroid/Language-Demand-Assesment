package com.languageempire.assessment.domain.model

data class LanguageDemand(
    val id: String,
    val languageName: String,
    val bookingType: BookingType,
    val totalRequests: Int,
    val availableInterpreters: Int,
    val unassignedBookings: Int,
    val averageWaitingTimeMinutes: Int,
    val riskLevel: RiskLevel
) {
    init {
        require(id.isNotBlank()) {
            "Language demand id cannot be blank."
        }

        require(languageName.isNotBlank()) {
            "Language name cannot be blank."
        }

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
    }
}