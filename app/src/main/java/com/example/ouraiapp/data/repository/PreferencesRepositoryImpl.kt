package com.example.ouraiapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ouraiapp.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "interview_prep_preferences")

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    override val onboardingCompleted: Flow<Boolean> =
        context.dataStore.data.map { preferences -> preferences[ONBOARDING_COMPLETED] ?: false }

    override val aiExplanationUsageCount: Flow<Int> =
        context.dataStore.data.map { preferences -> preferences[AI_EXPLANATION_USAGE_COUNT] ?: 0 }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    override suspend fun incrementAiExplanationUsage(): Int {
        var updatedCount = 0
        context.dataStore.edit { preferences ->
            updatedCount = (preferences[AI_EXPLANATION_USAGE_COUNT] ?: 0) + 1
            preferences[AI_EXPLANATION_USAGE_COUNT] = updatedCount
        }
        return updatedCount
    }

    private companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val AI_EXPLANATION_USAGE_COUNT = intPreferencesKey("ai_explanation_usage_count")
    }
}
