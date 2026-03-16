package com.example.ouraiapp.domain.model

data class AnswerOption(
    val id: String,
    val text: String
)

data class Question(
    val id: Int,
    val topic: String,
    val difficultyLevel: DifficultyLevel,
    val question: String,
    val options: List<AnswerOption>,
    val correctAnswer: String,
    val explanation: String
)
