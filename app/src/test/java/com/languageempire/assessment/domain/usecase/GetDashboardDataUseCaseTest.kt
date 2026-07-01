package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.BookingType
import com.languageempire.assessment.domain.model.LanguageDemand
import com.languageempire.assessment.domain.model.RiskLevel
import com.languageempire.assessment.fake.FakeDemandRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetDashboardDataUseCaseTest {

    @Test
    fun `loads language demand once and calculates booking type stats from same data`() = runTest {
        val repository = FakeDemandRepository(
            languageDemands = createLanguageDemands()
        )

        val useCase = createUseCase(
            repository = repository
        )

        val result = useCase()

        assertEquals(1, repository.getLanguageDemandCallCount)
        assertEquals(2, result.languageDemands.size)
        assertEquals(BookingType.entries.size, result.bookingTypeStats.size)
    }

    @Test
    fun `returns booking stats calculated from loaded language demand`() = runTest {
        val repository = FakeDemandRepository(
            languageDemands = createLanguageDemands()
        )

        val useCase = createUseCase(
            repository = repository
        )

        val result = useCase()

        val telephoneStats = result.bookingTypeStats.first { stats ->
            stats.bookingType == BookingType.TELEPHONE
        }

        assertEquals(320, telephoneStats.totalBookings)
        assertEquals(45, telephoneStats.unassignedBookings)
        assertEquals(RiskLevel.RED, telephoneStats.riskLevel)
    }

    @Test
    fun `returns green zero stats for missing booking types`() = runTest {
        val repository = FakeDemandRepository(
            languageDemands = listOf(
                LanguageDemand(
                    id = "arabic_telephone",
                    languageName = "Arabic",
                    bookingType = BookingType.TELEPHONE,
                    totalRequests = 320,
                    availableInterpreters = 18,
                    unassignedBookings = 45,
                    averageWaitingTimeMinutes = 18,
                    riskLevel = RiskLevel.RED
                )
            )
        )

        val useCase = createUseCase(
            repository = repository
        )

        val result = useCase()

        val translationStats = result.bookingTypeStats.first { stats ->
            stats.bookingType == BookingType.TRANSLATION
        }

        assertEquals(0, translationStats.totalBookings)
        assertEquals(0, translationStats.unassignedBookings)
        assertEquals(RiskLevel.GREEN, translationStats.riskLevel)
    }

    private fun createUseCase(
        repository: FakeDemandRepository
    ): GetDashboardDataUseCase {
        val getLanguageDemandUseCase = GetLanguageDemandUseCase(
            demandRepository = repository
        )

        val calculateBookingTypeStatsUseCase = CalculateBookingTypeStatsUseCase(
            calculateRiskLevelUseCase = CalculateRiskLevelUseCase()
        )

        return GetDashboardDataUseCase(
            getLanguageDemandUseCase = getLanguageDemandUseCase,
            calculateBookingTypeStatsUseCase = calculateBookingTypeStatsUseCase
        )
    }

    private fun createLanguageDemands(): List<LanguageDemand> {
        return listOf(
            LanguageDemand(
                id = "arabic_telephone",
                languageName = "Arabic",
                bookingType = BookingType.TELEPHONE,
                totalRequests = 320,
                availableInterpreters = 18,
                unassignedBookings = 45,
                averageWaitingTimeMinutes = 18,
                riskLevel = RiskLevel.RED
            ),
            LanguageDemand(
                id = "polish_face_to_face",
                languageName = "Polish",
                bookingType = BookingType.FACE_TO_FACE,
                totalRequests = 110,
                availableInterpreters = 40,
                unassignedBookings = 3,
                averageWaitingTimeMinutes = 4,
                riskLevel = RiskLevel.GREEN
            )
        )
    }
}