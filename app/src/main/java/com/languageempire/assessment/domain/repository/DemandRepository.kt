package com.languageempire.assessment.domain.repository

import com.languageempire.assessment.domain.model.LanguageDemand

interface DemandRepository {

    suspend fun getLanguageDemand(): List<LanguageDemand>
}