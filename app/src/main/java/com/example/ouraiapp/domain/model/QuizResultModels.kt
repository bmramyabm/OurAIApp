package com.example.ouraiapp.domain.model

data class QuestionResult(
    val question: Question,
    val selectedAnswer: String?,
    val isCorrect: Boolean,
    val isSkipped: Boolean
)

data class QuizSummary(
    val difficultyLevel: DifficultyLevel,
    val totalQuestions: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val skippedCount: Int,
    val results: List<QuestionResult>
)
