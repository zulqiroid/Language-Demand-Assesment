package com.languageempire.assessment.presentation.call.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.languageempire.assessment.domain.model.CallFailureReason

@Immutable
data class CallFailureReasonUiModel(
    val reason: CallFailureReason,
    @StringRes val messageRes: Int
)