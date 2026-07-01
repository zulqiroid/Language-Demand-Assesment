package com.languageempire.assessment.core.result

sealed interface AppError {
    data object Unknown : AppError
    data object Timeout : AppError
    data object Cancelled : AppError

    data class Unexpected(
        val cause: Throwable? = null
    ) : AppError
}