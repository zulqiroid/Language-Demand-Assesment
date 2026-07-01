package com.languageempire.assessment.data.provider

import com.languageempire.assessment.data.source.CallProviderScenarioDataSource
import com.languageempire.assessment.domain.model.CallConnectionResult
import com.languageempire.assessment.domain.model.CallFailureReason
import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.provider.CallProvider
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

abstract class BaseMockCallProvider(
    private val scenarioDataSource: CallProviderScenarioDataSource
) : CallProvider {

    final override suspend fun connect(): CallConnectionResult {
        return when (scenarioDataSource.getBehavior(providerType)) {
            CallProviderBehavior.SUCCESS -> connectSuccessfully()
            CallProviderBehavior.UNAVAILABLE -> failAsUnavailable()
            CallProviderBehavior.REJECTED -> failAsRejected()
            CallProviderBehavior.CRASH -> crashProvider()
            CallProviderBehavior.TIMEOUT -> simulateTimeout()
        }
    }

    private suspend fun connectSuccessfully(): CallConnectionResult {
        delay(MockCallProviderDefaults.CONNECTION_DELAY_MILLIS.milliseconds)

        return CallConnectionResult.Connected(
            providerType = providerType
        )
    }

    private suspend fun failAsUnavailable(): CallConnectionResult {
        delay(MockCallProviderDefaults.CONNECTION_DELAY_MILLIS.milliseconds)

        return CallConnectionResult.Failed(
            providerType = providerType,
            reason = CallFailureReason.ProviderUnavailable
        )
    }

    private suspend fun failAsRejected(): CallConnectionResult {
        delay(MockCallProviderDefaults.CONNECTION_DELAY_MILLIS.milliseconds)

        return CallConnectionResult.Failed(
            providerType = providerType,
            reason = CallFailureReason.ConnectionRejected
        )
    }

    private suspend fun crashProvider(): CallConnectionResult {
        delay(MockCallProviderDefaults.CONNECTION_DELAY_MILLIS.milliseconds)

        error("Mock provider crashed.")
    }

    private suspend fun simulateTimeout(): CallConnectionResult {
        delay(MockCallProviderDefaults.TIMEOUT_SIMULATION_DELAY_MILLIS.milliseconds)

        return CallConnectionResult.Failed(
            providerType = providerType,
            reason = CallFailureReason.Timeout
        )
    }
}