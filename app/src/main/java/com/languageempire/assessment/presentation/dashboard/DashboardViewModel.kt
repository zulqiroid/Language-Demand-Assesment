package com.languageempire.assessment.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.languageempire.assessment.R
import com.languageempire.assessment.domain.usecase.GetDashboardDataUseCase
import com.languageempire.assessment.presentation.dashboard.mapper.DashboardUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val dashboardUiMapper: DashboardUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(
        DashboardUiState.Loading
    )

    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun onAction(
        action: DashboardAction
    ) {
        when (action) {
            DashboardAction.Retry -> loadDashboardData()
        }
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading

            try {
                val dashboardData = getDashboardDataUseCase()

                _uiState.value = DashboardUiState.Content(
                    summary = dashboardUiMapper.mapSummary(
                        demands = dashboardData.languageDemands
                    ),
                    languageDemands = dashboardUiMapper.mapLanguageDemands(
                        demands = dashboardData.languageDemands
                    ),
                    bookingTypeStats = dashboardUiMapper.mapBookingTypeStats(
                        stats = dashboardData.bookingTypeStats
                    )
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Throwable) {
                _uiState.value = DashboardUiState.Error(
                    messageRes = R.string.dashboard_error_generic
                )
            }
        }
    }
}