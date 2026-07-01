package com.languageempire.assessment.fake

import com.languageempire.assessment.domain.model.CallFailoverEvent
import com.languageempire.assessment.domain.repository.FailoverLogger

class RecordingFailoverLogger(
    private val shouldThrow: Boolean = false
) : FailoverLogger {

    private val mutableEvents = mutableListOf<CallFailoverEvent>()

    val events: List<CallFailoverEvent>
        get() = mutableEvents.toList()

    override suspend fun log(event: CallFailoverEvent) {
        if (shouldThrow) {
            error("Logger failed.")
        }

        mutableEvents.add(event)
    }
}