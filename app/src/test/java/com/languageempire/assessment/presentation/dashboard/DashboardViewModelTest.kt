package com.languageempire.assessment.presentation.dashboard

import com.languageempire.assessment.core.coroutine.MainDispatcherRule
import com.languageempire.assessment.domain.model.BookingType
import com.languageempire.assessment.domain.model.LanguageDemand
import com.languageempire.assessment.domain.model.RiskLevel
import com.languageempire.assessment.domain.usecase.CalculateRiskLevelUseCase
import com.languageempire.assessment.domain.usecase.CalculateBookingTypeStatsUseCase
import com.languageempire.assessment.domain.usecase.GetDashboardDataUseCase
import com.languageempire.assessment.domain.usecase.GetLanguageDemandUseCase
import com.languageempire.assessment.fake.FakeDemandRepository
import com.languageempire.assessment.presentation.dashboard.mapper.DashboardUiMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads dashboard content on init`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val repository = FakeDemandRepository(
            languageDemands = createLanguageDemands()
        )

        val viewModel = createViewModel(
            repository = repository
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state is DashboardUiState.Content)

        val content = state as DashboardUiState.Content

        assertEquals(2, content.languageDemands.size)
        assertEquals(5, content.bookingTypeStats.size)
        assertEquals(300, content.summary.totalRequests)
        assertEquals(55, content.summary.totalAvailableInterpreters)
        assertEquals(25, content.summary.totalUnassignedBookings)
        assertEquals(1, content.summary.redRiskLanguagesCount)
    }

    @Test
    fun `emits error when dashboard loading fails`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val repository = FakeDemandRepository(
            throwable = IllegalStateException("Dashboard failed.")
        )

        val viewModel = createViewModel(
            repository = repository
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state is DashboardUiState.Error)
    }

    @Test
    fun `retry reloads dashboard data after error`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val repository = FakeDemandRepository(
            throwable = IllegalStateException("Dashboard failed.")
        )

        val viewModel = createViewModel(
            repository = repository
        )

        advanceUntilIdle()

        viewModel.onAction(DashboardAction.Retry)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is DashboardUiState.Error)
        assertEquals(2, repository.getLanguageDemandCallCount)
    }

    private fun createViewModel(
        repository: FakeDemandRepository
    ): DashboardViewModel {
        val calculateRiskLevelUseCase = CalculateRiskLevelUseCase()

        val getLanguageDemandUseCase = GetLanguageDemandUseCase(
            demandRepository = repository
        )

        val calculateBookingTypeStatsUseCase = CalculateBookingTypeStatsUseCase(
            calculateRiskLevelUseCase = calculateRiskLevelUseCase
        )

        val getDashboardDataUseCase = GetDashboardDataUseCase(
            getLanguageDemandUseCase = getLanguageDemandUseCase,
            calculateBookingTypeStatsUseCase = calculateBookingTypeStatsUseCase
        )

        return DashboardViewModel(
            getDashboardDataUseCase = getDashboardDataUseCase,
            dashboardUiMapper = DashboardUiMapper()
        )
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
                id = "polish_face_to_face",
                languageName = "Polish",
                bookingType = BookingType.FACE_TO_FACE,
                totalRequests = 100,
                availableInterpreters = 40,
                unassignedBookings = 3,
                averageWaitingTimeMinutes = 4,
                riskLevel = RiskLevel.GREEN
            )
        )
    }
}