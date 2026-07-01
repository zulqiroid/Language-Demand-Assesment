package com.languageempire.assessment.presentation.call.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.languageempire.assessment.domain.model.CallProviderBehavior

@Immutable
data class ProviderBehaviorUiModel(
    val behavior: CallProviderBehavior,
    @StringRes val labelRes: Int
)