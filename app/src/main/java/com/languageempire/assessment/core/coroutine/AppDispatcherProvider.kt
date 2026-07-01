package com.languageempire.assessment.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher

internal class AppDispatcherProvider(
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : DispatcherProvider {

    override val main: CoroutineDispatcher
        get() = mainDispatcher

    override val io: CoroutineDispatcher
        get() = ioDispatcher

    override val default: CoroutineDispatcher
        get() = defaultDispatcher
}