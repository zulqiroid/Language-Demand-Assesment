package com.languageempire.assessment.di

import com.languageempire.assessment.core.time.SystemTimeProvider
import com.languageempire.assessment.core.time.TimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeModule {

    @Binds
    @Singleton
    abstract fun bindTimeProvider(
        implementation: SystemTimeProvider
    ): TimeProvider

}