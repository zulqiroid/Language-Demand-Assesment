package com.languageempire.assessment.core.time

import javax.inject.Inject

class SystemTimeProvider @Inject constructor() : TimeProvider {

    override fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}