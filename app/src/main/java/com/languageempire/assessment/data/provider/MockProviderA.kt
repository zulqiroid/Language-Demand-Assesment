package com.languageempire.assessment.data.provider

import com.languageempire.assessment.data.source.CallProviderScenarioDataSource
import com.languageempire.assessment.domain.model.CallProviderType
import javax.inject.Inject

class MockProviderA @Inject constructor(
    scenarioDataSource: CallProviderScenarioDataSource
) : BaseMockCallProvider(
    scenarioDataSource = scenarioDataSource
) {
    override val providerType: CallProviderType = CallProviderType.PROVIDER_A
}