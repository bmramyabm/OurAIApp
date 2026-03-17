package com.example.ouraiapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val onboardingCompleted: Flow<Boolean>
    val aiExplanationUsageCount: Flow<Int>
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun incrementAiExplanationUsage(): Int
}
