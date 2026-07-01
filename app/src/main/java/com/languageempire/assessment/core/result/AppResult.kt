package com.languageempire.assessment.core.result

sealed interface AppResult<out T> {

    data class Success<out T>(
        val data: T
    ) : AppResult<T>

    data class Failure(
        val error: AppError
    ) : AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.map(
    transform: (T) -> R
): AppResult<R> {
    return when (this) {
        is AppResult.Success -> AppResult.Success(
            data = transform(data)
        )

        is AppResult.Failure -> this
    }
}

inline fun <T> AppResult<T>.onSuccess(
    action: (T) -> Unit
): AppResult<T> {
    if (this is AppResult.Success) {
        action(data)
    }

    return this
}

inline fun <T> AppResult<T>.onFailure(
    action: (AppError) -> Unit
): AppResult<T> {
    if (this is AppResult.Failure) {
        action(error)
    }

    return this
}