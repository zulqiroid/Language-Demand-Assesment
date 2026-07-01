package com.languageempire.assessment.presentation.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.domain.usecase.GetCallProviderScenarioUseCase
import com.languageempire.assessment.domain.usecase.StartCallWithFailoverUseCase
import com.languageempire.assessment.domain.usecase.UpdateCallProviderBehaviorUseCase
import com.languageempire.assessment.presentation.call.mapper.CallFailoverUiMapper
import com.languageempire.assessment.presentation.call.mapper.CallProviderScenarioUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CallFailoverViewModel @Inject constructor(
    private val startCallWithFailoverUseCase: StartCallWithFailoverUseCase,
    private val getCallProviderScenarioUseCase: GetCallProviderScenarioUseCase,
    private val updateCallProviderBehaviorUseCase: UpdateCallProviderBehaviorUseCase,
    private val callFailoverUiMapper: CallFailoverUiMapper,
    private val callProviderScenarioUiMapper: CallProviderScenarioUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<CallFailoverUiState>(
        CallFailoverUiState.Idle
    )

    val uiState: StateFlow<CallFailoverUiState> = _uiState.asStateFlow()

    private val _scenarioUiState = MutableStateFlow(
        callProviderScenarioUiMapper.mapScenario(
            scenario = getCallProviderScenarioUseCase()
        )
    )

    val scenarioUiState: StateFlow<CallProviderScenarioUiState> = _scenarioUiState.asStateFlow()

    private var activeCallJob: Job? = null

    fun onAction(
        action: CallFailoverAction
    ) {
        when (action) {
            CallFailoverAction.StartCall -> startCall()
            CallFailoverAction.Reset -> reset()

            is CallFailoverAction.UpdateProviderBehavior -> {
                updateProviderBehavior(
                    providerType = action.providerType,
                    behavior = action.behavior
                )
            }
        }
    }

    private fun startCall() {
        if (activeCallJob?.isActive == true) {
            return
        }

        activeCallJob = viewModelScope.launch {
            try {
                startCallWithFailoverUseCase()
                    .collect { status ->
                        _uiState.value = callFailoverUiMapper.mapStatus(
                            status = status
                        )
                    }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Throwable) {
                _uiState.value = CallFailoverUiState.Idle
            }
        }
    }

    private fun updateProviderBehavior(
        providerType: CallProviderType,
        behavior: CallProviderBehavior
    ) {
        if (activeCallJob?.isActive == true) {
            return
        }

        updateCallProviderBehaviorUseCase(
            providerType = providerType,
            behavior = behavior
        )

        _scenarioUiState.value = callProviderScenarioUiMapper.mapScenario(
            scenario = getCallProviderScenarioUseCase()
        )

        _uiState.value = CallFailoverUiState.Idle
    }

    private fun reset() {
        activeCallJob?.cancel()
        activeCallJob = null
        _uiState.value = CallFailoverUiState.Idle
    }

    override fun onCleared() {
        activeCallJob?.cancel()
        activeCallJob = null
        super.onCleared()
    }
}