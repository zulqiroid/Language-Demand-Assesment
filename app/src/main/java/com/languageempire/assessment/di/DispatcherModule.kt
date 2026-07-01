package com.languageempire.assessment.di

import com.languageempire.assessment.core.coroutine.AppDispatcherProvider
import com.languageempire.assessment.core.coroutine.DefaultDispatcher
import com.languageempire.assessment.core.coroutine.DispatcherProvider
import com.languageempire.assessment.core.coroutine.IoDispatcher
import com.languageempire.assessment.core.coroutine.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main.immediate
    }

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): DispatcherProvider {
        return AppDispatcherProvider(
            mainDispatcher = mainDispatcher,
            ioDispatcher = ioDispatcher,
            defaultDispatcher = defaultDispatcher
        )
    }
}