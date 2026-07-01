package com.languageempire.assessment.domain.model

data class BookingTypeStats(
    val bookingType: BookingType,
    val totalBookings: Int,
    val unassignedBookings: Int,
    val riskLevel: RiskLevel
) {
    init {
        require(totalBookings >= 0) {
            "Total bookings cannot be negative."
        }

        require(unassignedBookings >= 0) {
            "Unassigned bookings cannot be negative."
        }

        require(unassignedBookings <= totalBookings) {
            "Unassigned bookings cannot be greater than total bookings."
        }
    }
}