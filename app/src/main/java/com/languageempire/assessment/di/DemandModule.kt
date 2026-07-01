package com.languageempire.assessment.di

import com.languageempire.assessment.data.repository.DemandRepositoryImpl
import com.languageempire.assessment.data.source.LanguageDemandDataSource
import com.languageempire.assessment.data.source.LocalLanguageDemandDataSource
import com.languageempire.assessment.domain.repository.DemandRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DemandModule {

    @Binds
    @Singleton
    abstract fun bindLanguageDemandDataSource(
        implementation: LocalLanguageDemandDataSource
    ): LanguageDemandDataSource

    @Binds
    @Singleton
    abstract fun bindDemandRepository(
        implementation: DemandRepositoryImpl
    ): DemandRepository
}