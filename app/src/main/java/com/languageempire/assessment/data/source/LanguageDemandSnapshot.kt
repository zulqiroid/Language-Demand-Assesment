package com.languageempire.assessment.data.source

import com.languageempire.assessment.domain.model.BookingType

data class LanguageDemandSnapshot(
    val id: String,
    val languageName: String,
    val bookingType: BookingType,
    val totalRequests: Int,
    val availableInterpreters: Int,
    val unassignedBookings: Int,
    val averageWaitingTimeMinutes: Int
)