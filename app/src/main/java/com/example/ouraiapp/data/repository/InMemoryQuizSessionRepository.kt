package com.example.ouraiapp.data.repository

import com.example.ouraiapp.domain.model.QuizSummary
import com.example.ouraiapp.domain.repository.QuizSessionRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class InMemoryQuizSessionRepository @Inject constructor() : QuizSessionRepository {

    private val latestSummaryFlow = MutableStateFlow<QuizSummary?>(null)

    override fun saveSummary(summary: QuizSummary) {
        latestSummaryFlow.value = summary
    }

    override fun latestSummary(): Flow<QuizSummary?> = latestSummaryFlow.asStateFlow()
}
