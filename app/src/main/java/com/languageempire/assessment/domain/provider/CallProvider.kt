package com.languageempire.assessment.domain.provider

import com.languageempire.assessment.domain.model.CallConnectionResult
import com.languageempire.assessment.domain.model.CallProviderType

interface CallProvider {

    val providerType: CallProviderType

    suspend fun connect(): CallConnectionResult
}