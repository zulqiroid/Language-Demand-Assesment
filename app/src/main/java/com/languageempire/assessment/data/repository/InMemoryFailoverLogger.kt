package com.languageempire.assessment.data.repository

import com.languageempire.assessment.domain.model.CallFailoverEvent
import com.languageempire.assessment.domain.repository.FailoverLogger
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryFailoverLogger @Inject constructor() : FailoverLogger {

    private val mutex = Mutex()
    private val events = mutableListOf<CallFailoverEvent>()

    override suspend fun log(
        event: CallFailoverEvent
    ) {
        mutex.withLock {
            events.add(event)
        }
    }
}