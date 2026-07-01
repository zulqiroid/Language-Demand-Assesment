package com.languageempire.assessment.data.source

import com.languageempire.assessment.domain.model.BookingType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalLanguageDemandDataSource @Inject constructor() : LanguageDemandDataSource {

    override suspend fun getLanguageDemandSnapshots(): List<LanguageDemandSnapshot> {
        return LANGUAGE_DEMAND_SNAPSHOTS
    }

    private companion object {
        val LANGUAGE_DEMAND_SNAPSHOTS = listOf(
            LanguageDemandSnapshot(
                id = "language_arabic_telephone",
                languageName = "Arabic",
                bookingType = BookingType.TELEPHONE,
                totalRequests = 320,
                availableInterpreters = 18,
                unassignedBookings = 45,
                averageWaitingTimeMinutes = 18
            ),
            LanguageDemandSnapshot(
                id = "language_urdu_video",
                languageName = "Urdu",
                bookingType = BookingType.VIDEO_REMOTE_INTERPRETING,
                totalRequests = 280,
                availableInterpreters = 25,
                unassignedBookings = 22,
                averageWaitingTimeMinutes = 12
            ),
            LanguageDemandSnapshot(
                id = "language_polish_face_to_face",
                languageName = "Polish",
                bookingType = BookingType.FACE_TO_FACE,
                totalRequests = 110,
                availableInterpreters = 40,
                unassignedBookings = 3,
                averageWaitingTimeMinutes = 4
            ),
            LanguageDemandSnapshot(
                id = "language_bsl_bsl",
                languageName = "BSL",
                bookingType = BookingType.VIDEO_REMOTE_INTERPRETING,
                totalRequests = 90,
                availableInterpreters = 8,
                unassignedBookings = 20,
                averageWaitingTimeMinutes = 20
            ),
            LanguageDemandSnapshot(
                id = "language_french_translation",
                languageName = "French",
                bookingType = BookingType.TRANSLATION,
                totalRequests = 60,
                availableInterpreters = 15,
                unassignedBookings = 5,
                averageWaitingTimeMinutes = 6
            )
        )
    }
}