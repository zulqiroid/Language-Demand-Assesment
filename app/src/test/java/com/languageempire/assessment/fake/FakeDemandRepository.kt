package com.languageempire.assessment.fake

import com.languageempire.assessment.domain.model.LanguageDemand
import com.languageempire.assessment.domain.repository.DemandRepository

class FakeDemandRepository(
    private val languageDemands: List<LanguageDemand> = emptyList(),
    private val throwable: Throwable? = null
) : DemandRepository {

    var getLanguageDemandCallCount: Int = 0
        private set

    override suspend fun getLanguageDemand(): List<LanguageDemand> {
        getLanguageDemandCallCount++

        throwable?.let { error ->
            throw error
        }

        return languageDemands
    }
}