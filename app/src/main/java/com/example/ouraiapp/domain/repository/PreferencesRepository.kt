package com.example.ouraiapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val onboardingCompleted: Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
}
