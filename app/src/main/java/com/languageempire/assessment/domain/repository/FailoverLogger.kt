package com.languageempire.assessment.domain.repository

import com.languageempire.assessment.domain.model.CallFailoverEvent

interface FailoverLogger {

    suspend fun log(event: CallFailoverEvent)
}