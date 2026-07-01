package com.languageempire.assessment.presentation.dashboard.mapper

import com.languageempire.assessment.R
import com.languageempire.assessment.domain.model.BookingType
import com.languageempire.assessment.domain.model.BookingTypeStats
import com.languageempire.assessment.domain.model.LanguageDemand
import com.languageempire.assessment.domain.model.RiskLevel
import com.languageempire.assessment.presentation.dashboard.model.BookingTypeStatsUiModel
import com.languageempire.assessment.presentation.dashboard.model.DashboardSummaryUiModel
import com.languageempire.assessment.presentation.dashboard.model.LanguageDemandUiModel
import com.languageempire.assessment.presentation.dashboard.model.RiskLevelUiModel
import javax.inject.Inject

class DashboardUiMapper @Inject constructor() {

    fun mapLanguageDemands(
        demands: List<LanguageDemand>
    ): List<LanguageDemandUiModel> {
        return demands.map(::mapLanguageDemand)
    }

    fun mapBookingTypeStats(
        stats: List<BookingTypeStats>
    ): List<BookingTypeStatsUiModel> {
        return stats.map(::mapBookingTypeStat)
    }

    fun mapSummary(
        demands: List<LanguageDemand>
    ): DashboardSummaryUiModel {
        val totalRequests = demands.sumOf { it.totalRequests }
        val totalAvailableInterpreters = demands.sumOf { it.availableInterpreters }
        val totalUnassignedBookings = demands.sumOf { it.unassignedBookings }
        val redRiskLanguagesCount = demands.count { it.riskLevel == RiskLevel.RED }

        return DashboardSummaryUiModel(
            totalRequests = totalRequests,
            totalAvailableInterpreters = totalAvailableInterpreters,
            totalUnassignedBookings = totalUnassignedBookings,
            averageWaitingTimeMinutes = calculateWeightedAverageWaitingTime(
                demands = demands,
                totalRequests = totalRequests
            ),
            redRiskLanguagesCount = redRiskLanguagesCount
        )
    }

    private fun mapLanguageDemand(
        demand: LanguageDemand
    ): LanguageDemandUiModel {
        return LanguageDemandUiModel(
            id = demand.id,
            languageName = demand.languageName,
            bookingTypeLabelRes = demand.bookingType.toLabelRes(),
            totalRequests = demand.totalRequests,
            availableInterpreters = demand.availableInterpreters,
            unassignedBookings = demand.unassignedBookings,
            averageWaitingTimeMinutes = demand.averageWaitingTimeMinutes,
            riskLevel = demand.riskLevel.toUiModel()
        )
    }

    private fun mapBookingTypeStat(
        stat: BookingTypeStats
    ): BookingTypeStatsUiModel {
        return BookingTypeStatsUiModel(
            bookingType = stat.bookingType,
            bookingTypeLabelRes = stat.bookingType.toLabelRes(),
            totalBookings = stat.totalBookings,
            unassignedBookings = stat.unassignedBookings,
            riskLevel = stat.riskLevel.toUiModel()
        )
    }

    private fun calculateWeightedAverageWaitingTime(
        demands: List<LanguageDemand>,
        totalRequests: Int
    ): Int {
        if (demands.isEmpty() || totalRequests == 0) {
            return ZERO_WAITING_TIME_MINUTES
        }

        val weightedWaitingTime = demands.sumOf { demand ->
            demand.averageWaitingTimeMinutes * demand.totalRequests
        }

        return weightedWaitingTime / totalRequests
    }

    private fun BookingType.toLabelRes(): Int {
        return when (this) {
            BookingType.FACE_TO_FACE -> R.string.booking_type_face_to_face
            BookingType.TELEPHONE -> R.string.booking_type_telephone
            BookingType.VIDEO_REMOTE_INTERPRETING -> R.string.booking_type_video_remote_interpreting
            BookingType.BSL -> R.string.booking_type_bsl
            BookingType.TRANSLATION -> R.string.booking_type_translation
        }
    }

    private fun RiskLevel.toUiModel(): RiskLevelUiModel {
        return RiskLevelUiModel(
            level = this,
            labelRes = when (this) {
                RiskLevel.GREEN -> R.string.risk_green
                RiskLevel.AMBER -> R.string.risk_amber
                RiskLevel.RED -> R.string.risk_red
            }
        )
    }

    private companion object {
        const val ZERO_WAITING_TIME_MINUTES = 0
    }
}