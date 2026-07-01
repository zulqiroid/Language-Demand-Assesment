package com.languageempire.assessment.domain.usecase

import com.languageempire.assessment.domain.model.CallProviderBehavior
import com.languageempire.assessment.domain.model.CallProviderType
import com.languageempire.assessment.domain.repository.CallProviderScenarioRepository
import javax.inject.Inject

class UpdateCallProviderBehaviorUseCase @Inject constructor(
    private val repository: CallProviderScenarioRepository
) {

    operator fun invoke(
        providerType: CallProviderType,
        behavior: CallProviderBehavior
    ) {
        repository.updateProviderBehavior(
            providerType = providerType,
            behavior = behavior
        )
    }
}