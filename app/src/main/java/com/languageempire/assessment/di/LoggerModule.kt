package com.languageempire.assessment.di

import com.languageempire.assessment.data.repository.InMemoryFailoverLogger
import com.languageempire.assessment.domain.repository.FailoverLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerModule {

    @Binds
    @Singleton
    abstract fun bindFailoverLogger(
        implementation: InMemoryFailoverLogger
    ): FailoverLogger
}