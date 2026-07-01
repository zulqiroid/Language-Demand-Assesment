package com.languageempire.assessment.fake

import com.languageempire.assessment.domain.model.CallConnectionResult
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.domain.provider.CallProvider
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class FakeCallProvider(
    override val providerType: CallProviderType,
    private val result: CallConnectionResult? = null,
    private val throwable: Throwable? = null,
    private val delayMillis: Long = 0L
) : CallProvider {

    var connectCallCount: Int = 0
        private set

    override suspend fun connect(): CallConnectionResult {
        connectCallCount++

        if (delayMillis > 0L) {
            delay(delayMillis.milliseconds)
        }

        throwable?.let { error ->
            throw error
        }

        return requireNotNull(result) {
            "FakeCallProvider requires either a result or a throwable."
        }
    }
}