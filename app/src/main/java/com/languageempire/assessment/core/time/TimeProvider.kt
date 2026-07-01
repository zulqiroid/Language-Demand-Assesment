package com.languageempire.assessment.core.time

interface TimeProvider {
    fun currentTimeMillis(): Long
}