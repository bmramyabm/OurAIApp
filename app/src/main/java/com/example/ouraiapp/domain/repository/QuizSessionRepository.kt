package com.example.ouraiapp.domain.repository

import com.example.ouraiapp.domain.model.QuizSummary
import kotlinx.coroutines.flow.Flow

interface QuizSessionRepository {
    fun saveSummary(summary: QuizSummary)
    fun latestSummary(): Flow<QuizSummary?>
}
