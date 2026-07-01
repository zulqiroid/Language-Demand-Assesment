package com.languageempire.assessment.data.repository

import com.languageempire.assessment.core.coroutine.DispatcherProvider
import com.languageempire.assessment.data.source.LanguageDemandDataSource
import com.languageempire.assessment.data.source.LanguageDemandSnapshot
import com.languageempire.assessment.domain.model.LanguageDemand
import com.languageempire.assessment.domain.repository.DemandRepository
import com.languageempire.assessment.domain.usecase.CalculateRiskLevelUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DemandRepositoryImpl @Inject constructor(
    private val dataSource: LanguageDemandDataSource,
    private val calculateRiskLevelUseCase: CalculateRiskLevelUseCase,
    private val dispatcherProvider: DispatcherProvider
) : DemandRepository {

    override suspend fun getLanguageDemand(): List<LanguageDemand> {
        return withContext(dispatcherProvider.default) {
            dataSource.getLanguageDemandSnapshots()
                .map(::mapToDomain)
                .sortedWith(
                    compareByDescending<LanguageDemand> { it.riskLevel.ordinal }
                        .thenByDescending { it.unassignedBookings }
                        .thenByDescending { it.totalRequests }
                )
        }
    }

    private fun mapToDomain(
        snapshot: LanguageDemandSnapshot
    ): LanguageDemand {
        return LanguageDemand(
            id = snapshot.id,
            languageName = snapshot.languageName,
            bookingType = snapshot.bookingType,
            totalRequests = snapshot.totalRequests,
            availableInterpreters = snapshot.availableInterpreters,
            unassignedBookings = snapshot.unassignedBookings,
            averageWaitingTimeMinutes = snapshot.averageWaitingTimeMinutes,
            riskLevel = calculateRiskLevelUseCase(
                totalRequests = snapshot.totalRequests,
                availableInterpreters = snapshot.availableInterpreters,
                unassignedBookings = snapshot.unassignedBookings,
                averageWaitingTimeMinutes = snapshot.averageWaitingTimeMinutes
            )
        )
    }
}