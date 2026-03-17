package com.example.ouraiapp.ui.navigation

import com.example.ouraiapp.domain.model.DifficultyLevel

object AppDestinations {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val QUIZ_LENGTH = "quiz_length/{difficulty}"
    const val QUIZ = "quiz/{difficulty}/{questionCount}"
    const val REVIEW = "review"

    fun quizLengthRoute(difficultyLevel: DifficultyLevel): String {
        return "quiz_length/${difficultyLevel.value}"
    }

    fun quizRoute(difficultyLevel: DifficultyLevel, questionCount: Int): String {
        return "quiz/${difficultyLevel.value}/$questionCount"
    }
}
