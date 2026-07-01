package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.LanguageDemand
import com.languageempire.assessment.domain.repository.DemandRepository
import javax.inject.Inject

class GetLanguageDemandUseCase @Inject constructor(
    private val demandRepository: DemandRepository
) {

    suspend operator fun invoke(): List<LanguageDemand> {
        return demandRepository.getLanguageDemand()
    }
}