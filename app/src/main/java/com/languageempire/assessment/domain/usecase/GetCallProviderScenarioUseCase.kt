package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.CallProviderScenario
import com.languageempire.assessment.domain.repository.CallProviderScenarioRepository
import javax.inject.Inject

class GetCallProviderScenarioUseCase @Inject constructor(
    private val repository: CallProviderScenarioRepository
) {

    operator fun invoke(): CallProviderScenario {
        return repository.getScenario()
    }
}