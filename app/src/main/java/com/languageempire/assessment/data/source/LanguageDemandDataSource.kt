package com.languageempire.assessment.data.source

interface LanguageDemandDataSource {

    suspend fun getLanguageDemandSnapshots(): List<LanguageDemandSnapshot>
}