package com.languageempire.assessment.di

import com.languageempire.assessment.data.provider.MockProviderA
import com.languageempire.assessment.data.provider.MockProviderB
import com.languageempire.assessment.data.repository.CallProviderScenarioRepositoryImpl
import com.languageempire.assessment.data.source.CallProviderScenarioDataSource
import com.languageempire.assessment.data.source.InMemoryCallProviderScenarioDataSource
import com.languageempire.assessment.domain.provider.BackupCallProvider
import com.languageempire.assessment.domain.provider.CallProvider
import com.languageempire.assessment.domain.provider.PrimaryCallProvider
import com.languageempire.assessment.domain.repository.CallProviderScenarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProviderModule {

    @Binds
    @Singleton
    abstract fun bindCallProviderScenarioDataSource(
        implementation: InMemoryCallProviderScenarioDataSource
    ): CallProviderScenarioDataSource

    @Binds
    @Singleton
    abstract fun bindCallProviderScenarioRepository(
        implementation: CallProviderScenarioRepositoryImpl
    ): CallProviderScenarioRepository

    @Binds
    @Singleton
    @PrimaryCallProvider
    abstract fun bindPrimaryCallProvider(
        implementation: MockProviderA
    ): CallProvider

    @Binds
    @Singleton
    @BackupCallProvider
    abstract fun bindBackupCallProvider(
        implementation: MockProviderB
    ): CallProvider
}