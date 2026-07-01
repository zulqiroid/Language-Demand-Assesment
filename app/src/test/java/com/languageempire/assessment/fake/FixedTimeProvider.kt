package com.languageempire.assessment.fake

import com.languageempire.assessment.core.time.TimeProvider

class FixedTimeProvider(
    private val fixedTimeMillis: Long = DEFAULT_FIXED_TIME_MILLIS
) : TimeProvider {

    override fun currentTimeMillis(): Long {
        return fixedTimeMillis
    }

    private companion object {
        const val DEFAULT_FIXED_TIME_MILLIS = 1_700_000_000_000L
    }
}