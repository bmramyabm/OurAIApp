package com.example.ouraiapp.domain.repository

import com.example.ouraiapp.domain.model.DifficultyLevel
import com.example.ouraiapp.domain.model.Question

interface InterviewRepository {
    suspend fun getQuizQuestions(difficultyLevel: DifficultyLevel, count: Int): List<Question>
    suspend fun getQuestionCount(): Int
}
