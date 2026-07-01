package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.BookingType
import com.languageempire.assessment.domain.model.LanguageDemand
import com.languageempire.assessment.domain.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculateBookingTypeStatsUseCaseTest {

    private lateinit var useCase: CalculateBookingTypeStatsUseCase

    @Before
    fun setUp() {
        useCase = CalculateBookingTypeStatsUseCase(
            calculateRiskLevelUseCase = CalculateRiskLevelUseCase()
        )
    }

    @Test
    fun `returns stats for all booking types`() {
        val result = useCase(
            languageDemand = createLanguageDemands()
        )

        assertEquals(
            BookingType.entries.size,
            result.size
        )

        val returnedTypes = result.map { stat ->
            stat.bookingType
        }

        assertEquals(
            BookingType.entries.toList(),
            returnedTypes
        )
    }

    @Test
    fun `aggregates telephone booking stats correctly`() {
        val result = useCase(
            languageDemand = createLanguageDemands()
        )

        val telephoneStats = result.first { stat ->
            stat.bookingType == BookingType.TELEPHONE
        }

        assertEquals(300, telephoneStats.totalBookings)
        assertEquals(30, telephoneStats.unassignedBookings)
        assertEquals(RiskLevel.RED, telephoneStats.riskLevel)
    }

    @Test
    fun `aggregates video booking stats correctly`() {
        val result = useCase(
            languageDemand = createLanguageDemands()
        )

        val videoStats = result.first { stat ->
            stat.bookingType == BookingType.VIDEO_REMOTE_INTERPRETING
        }

        assertEquals(80, videoStats.totalBookings)
        assertEquals(5, videoStats.unassignedBookings)
        assertEquals(RiskLevel.GREEN, videoStats.riskLevel)
    }

    @Test
    fun `returns green risk for booking type with no demand`() {
        val result = useCase(
            languageDemand = createLanguageDemands()
        )

        val translationStats = result.first { stat ->
            stat.bookingType == BookingType.TRANSLATION
        }

        assertEquals(0, translationStats.totalBookings)
        assertEquals(0, translationStats.unassignedBookings)
        assertEquals(RiskLevel.GREEN, translationStats.riskLevel)
    }

    @Test
    fun `returns green zero stats for every booking type when demand list is empty`() {
        val result = useCase(
            languageDemand = emptyList()
        )

        result.forEach { stats ->
            assertEquals(0, stats.totalBookings)
            assertEquals(0, stats.unassignedBookings)
            assertEquals(RiskLevel.GREEN, stats.riskLevel)
        }
    }

    private fun createLanguageDemands(): List<LanguageDemand> {
        return listOf(
            LanguageDemand(
                id = "arabic_telephone",
                languageName = "Arabic",
                bookingType = BookingType.TELEPHONE,
                totalRequests = 200,
                availableInterpreters = 15,
                unassignedBookings = 22,
                averageWaitingTimeMinutes = 14,
                riskLevel = RiskLevel.RED
            ),
            LanguageDemand(
                id = "urdu_telephone",
                languageName = "Urdu",
                bookingType = BookingType.TELEPHONE,
                totalRequests = 100,
                availableInterpreters = 20,
                unassignedBookings = 8,
                averageWaitingTimeMinutes = 7,
                riskLevel = RiskLevel.AMBER
            ),
            LanguageDemand(
                id = "polish_video",
                languageName = "Polish",
                bookingType = BookingType.VIDEO_REMOTE_INTERPRETING,
                totalRequests = 80,
                availableInterpreters = 35,
                unassignedBookings = 5,
                averageWaitingTimeMinutes = 4,
                riskLevel = RiskLevel.GREEN
            )
        )
    }
}