package com.languageempire.assessment.domain.provider

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrimaryCallProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackupCallProvider