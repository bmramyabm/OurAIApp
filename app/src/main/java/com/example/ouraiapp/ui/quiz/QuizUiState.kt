package com.example.ouraiapp.ui.quiz

import com.example.ouraiapp.domain.model.DifficultyLevel
import com.example.ouraiapp.domain.model.Question
import com.example.ouraiapp.domain.model.QuestionResult

data class QuizUiState(
    val isLoading: Boolean = true,
    val difficultyLevel: DifficultyLevel = DifficultyLevel.JUNIOR,
    val requestedQuestionCount: Int = 10,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val answerSubmitted: Boolean = false,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val aiInsight: String? = null,
    val isAiLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCompleted: Boolean = false
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentIndex)

    val progress: Float
        get() = if (questions.isEmpty()) 0f else (currentIndex + 1) / questions.size.toFloat()
}

internal fun Map<Int, QuestionResult>.correctCount(): Int = values.count { it.isCorrect }
internal fun Map<Int, QuestionResult>.wrongCount(): Int = values.count { !it.isCorrect && !it.isSkipped }
