package com.example.ouraiapp.di

import com.example.ouraiapp.data.repository.AiRepositoryImpl
import com.example.ouraiapp.data.repository.InMemoryQuizSessionRepository
import com.example.ouraiapp.data.repository.InterviewRepositoryImpl
import com.example.ouraiapp.data.repository.PreferencesRepositoryImpl
import com.example.ouraiapp.domain.repository.AiRepository
import com.example.ouraiapp.domain.repository.InterviewRepository
import com.example.ouraiapp.domain.repository.PreferencesRepository
import com.example.ouraiapp.domain.repository.QuizSessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindInterviewRepository(
        implementation: InterviewRepositoryImpl
    ): InterviewRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        implementation: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindAiRepository(
        implementation: AiRepositoryImpl
    ): AiRepository

    @Binds
    @Singleton
    abstract fun bindQuizSessionRepository(
        implementation: InMemoryQuizSessionRepository
    ): QuizSessionRepository
}
