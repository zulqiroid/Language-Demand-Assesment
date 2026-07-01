package com.languageempire.assessment.presentation.call.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.languageempire.assessment.domain.model.CallProviderType

@Immutable
data class CallProviderUiModel(
    val type: CallProviderType,
    @StringRes val labelRes: Int
)