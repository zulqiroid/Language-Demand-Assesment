package com.languageempire.assessment.presentation.call

import com.languageempire.assessment.R
import com.languageempire.assessment.core.coroutine.MainDispatcherRule
import com.languageempire.assessment.core.coroutine.TestDispatcherProvider
import com.languageempire.assessment.domain.model.CallConnectionResult
import com.languageempire.assessment.domain.model.CallFailureReason
import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderScenario
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.domain.usecase.GetCallProviderScenarioUseCase
import com.languageempire.assessment.domain.usecase.StartCallWithFailoverUseCase
import com.languageempire.assessment.domain.usecase.UpdateCallProviderBehaviorUseCase
import com.languageempire.assessment.fake.FakeCallProvider
import com.languageempire.assessment.fake.FakeCallProviderScenarioRepository
import com.languageempire.assessment.fake.FixedTimeProvider
import com.languageempire.assessment.fake.RecordingFailoverLogger
import com.languageempire.assessment.presentation.call.mapper.CallFailoverUiMapper
import com.languageempire.assessment.presentation.call.mapper.CallProviderScenarioUiMapper
import com.languageempire.assessment.presentation.call.model.CallProviderUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CallFailoverViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state is idle`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val viewModel = createViewModel()

        assertEquals(
            CallFailoverUiState.Idle,
            viewModel.uiState.value
        )
    }

    @Test
    fun `starts call and connects with backup provider when primary fails`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Failed(
                providerType = CallProviderType.PROVIDER_A,
                reason = CallFailureReason.ProviderUnavailable
            )
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        )

        val viewModel = createViewModel(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider
        )

        viewModel.onAction(CallFailoverAction.StartCall)

        advanceUntilIdle()

        assertEquals(
            CallFailoverUiState.Connected(
                provider = CallProviderUiModel(
                    type = CallProviderType.PROVIDER_B,
                    labelRes = R.string.provider_b
                )
            ),
            viewModel.uiState.value
        )

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(1, backupProvider.connectCallCount)
    }

    @Test
    fun `does not call backup provider when primary provider succeeds`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_A
            )
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        )

        val viewModel = createViewModel(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider
        )

        viewModel.onAction(CallFailoverAction.StartCall)

        advanceUntilIdle()

        assertEquals(
            CallFailoverUiState.Connected(
                provider = CallProviderUiModel(
                    type = CallProviderType.PROVIDER_A,
                    labelRes = R.string.provider_a
                )
            ),
            viewModel.uiState.value
        )

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(0, backupProvider.connectCallCount)
    }

    @Test
    fun `emits failed state when both providers fail`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Failed(
                providerType = CallProviderType.PROVIDER_A,
                reason = CallFailureReason.ProviderUnavailable
            )
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Failed(
                providerType = CallProviderType.PROVIDER_B,
                reason = CallFailureReason.ConnectionRejected
            )
        )

        val viewModel = createViewModel(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider
        )

        viewModel.onAction(CallFailoverAction.StartCall)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is CallFailoverUiState.Failed)
        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(1, backupProvider.connectCallCount)
    }

    @Test
    fun `ignores duplicate start action while call is active`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_A
            ),
            delayMillis = ACTIVE_CALL_DELAY_MILLIS
        )

        val backupProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        )

        val viewModel = createViewModel(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider
        )

        viewModel.onAction(CallFailoverAction.StartCall)
        viewModel.onAction(CallFailoverAction.StartCall)

        advanceUntilIdle()

        assertEquals(1, primaryProvider.connectCallCount)
        assertEquals(0, backupProvider.connectCallCount)
    }

    @Test
    fun `reset cancels active call and returns to idle`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_A
            ),
            delayMillis = ACTIVE_CALL_DELAY_MILLIS
        )

        val viewModel = createViewModel(
            primaryProvider = primaryProvider
        )

        viewModel.onAction(CallFailoverAction.StartCall)
        viewModel.onAction(CallFailoverAction.Reset)

        advanceUntilIdle()

        assertEquals(
            CallFailoverUiState.Idle,
            viewModel.uiState.value
        )
    }

    @Test
    fun `updates provider behavior when call is idle`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val scenarioRepository = FakeCallProviderScenarioRepository()

        val viewModel = createViewModel(
            scenarioRepository = scenarioRepository
        )

        viewModel.onAction(
            CallFailoverAction.UpdateProviderBehavior(
                providerType = CallProviderType.PROVIDER_A,
                behavior = CallProviderBehavior.SUCCESS
            )
        )

        val scenarioState = viewModel.scenarioUiState.value

        assertEquals(
            CallProviderBehavior.SUCCESS,
            scenarioState.providerABehavior.behavior
        )

        assertEquals(
            CallFailoverUiState.Idle,
            viewModel.uiState.value
        )
    }

    @Test
    fun `does not update provider behavior while call is active`() = runTest(
        context = mainDispatcherRule.testDispatcher
    ) {
        val scenarioRepository = FakeCallProviderScenarioRepository(
            initialScenario = CallProviderScenario(
                providerABehavior = CallProviderBehavior.UNAVAILABLE,
                providerBBehavior = CallProviderBehavior.SUCCESS
            )
        )

        val primaryProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_A
            ),
            delayMillis = ACTIVE_CALL_DELAY_MILLIS
        )

        val viewModel = createViewModel(
            primaryProvider = primaryProvider,
            scenarioRepository = scenarioRepository
        )

        viewModel.onAction(CallFailoverAction.StartCall)

        viewModel.onAction(
            CallFailoverAction.UpdateProviderBehavior(
                providerType = CallProviderType.PROVIDER_A,
                behavior = CallProviderBehavior.SUCCESS
            )
        )

        advanceUntilIdle()

        assertEquals(
            CallProviderBehavior.UNAVAILABLE,
            viewModel.scenarioUiState.value.providerABehavior.behavior
        )
    }

    private fun createViewModel(
        primaryProvider: FakeCallProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_A,
            result = CallConnectionResult.Failed(
                providerType = CallProviderType.PROVIDER_A,
                reason = CallFailureReason.ProviderUnavailable
            )
        ),
        backupProvider: FakeCallProvider = FakeCallProvider(
            providerType = CallProviderType.PROVIDER_B,
            result = CallConnectionResult.Connected(
                providerType = CallProviderType.PROVIDER_B
            )
        ),
        scenarioRepository: FakeCallProviderScenarioRepository = FakeCallProviderScenarioRepository(),
        testDispatcher: TestDispatcher = mainDispatcherRule.testDispatcher
    ): CallFailoverViewModel {
        val startCallWithFailoverUseCase = StartCallWithFailoverUseCase(
            primaryProvider = primaryProvider,
            backupProvider = backupProvider,
            failoverLogger = RecordingFailoverLogger(),
            timeProvider = FixedTimeProvider(),
            dispatcherProvider = TestDispatcherProvider(
                testDispatcher = testDispatcher
            )
        )

        return CallFailoverViewModel(
            startCallWithFailoverUseCase = startCallWithFailoverUseCase,
            getCallProviderScenarioUseCase = GetCallProviderScenarioUseCase(
                repository = scenarioRepository
            ),
            updateCallProviderBehaviorUseCase = UpdateCallProviderBehaviorUseCase(
                repository = scenarioRepository
            ),
            callFailoverUiMapper = CallFailoverUiMapper(),
            callProviderScenarioUiMapper = CallProviderScenarioUiMapper()
        )
    }

    private companion object {
        const val ACTIVE_CALL_DELAY_MILLIS = 1_000L
    }
}