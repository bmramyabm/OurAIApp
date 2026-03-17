package com.example.ouraiapp.ui.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ouraiapp.domain.model.DifficultyLevel
import com.example.ouraiapp.domain.model.QuestionResult
import com.example.ouraiapp.domain.model.QuizSummary
import com.example.ouraiapp.domain.usecase.AskAiForQuestionUseCase
import com.example.ouraiapp.domain.usecase.GetQuizQuestionsUseCase
import com.example.ouraiapp.domain.usecase.SaveQuizSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuizQuestionsUseCase: GetQuizQuestionsUseCase,
    private val askAiForQuestionUseCase: AskAiForQuestionUseCase,
    private val saveQuizSummaryUseCase: SaveQuizSummaryUseCase
) : ViewModel() {

    private val difficultyLevel = DifficultyLevel.fromValue(savedStateHandle["difficulty"])
    private val requestedCount: Int = savedStateHandle["questionCount"] ?: 10

    private val answersByQuestionId = linkedMapOf<Int, QuestionResult>()
    private val quizStartTimeMillis = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(
        QuizUiState(
            difficultyLevel = difficultyLevel,
            requestedQuestionCount = requestedCount
        )
    )
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val questions = getQuizQuestionsUseCase(difficultyLevel, requestedCount).ifEmpty {
                getQuizQuestionsUseCase(difficultyLevel, 10)
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                questions = questions,
                errorMessage = if (questions.isEmpty()) {
                    "No questions found for ${difficultyLevel.title}. Seed more data in QuestionSeedData."
                } else {
                    null
                }
            )
        }
    }

    fun selectAnswer(answerId: String) {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        if (state.answerSubmitted) return

        val result = QuestionResult(
            question = question,
            selectedAnswer = answerId,
            isCorrect = answerId == question.correctAnswer,
            isSkipped = false
        )
        answersByQuestionId[question.id] = result

        _uiState.value = state.copy(
            selectedAnswer = answerId,
            answerSubmitted = true,
            correctCount = answersByQuestionId.correctCount(),
            wrongCount = answersByQuestionId.wrongCount(),
            aiInsight = null
        )
    }

    fun skipQuestion() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        if (state.answerSubmitted) return

        answersByQuestionId[question.id] = QuestionResult(
            question = question,
            selectedAnswer = null,
            isCorrect = false,
            isSkipped = true
        )
        moveToNextQuestion()
    }

    fun nextQuestion() {
        if (!_uiState.value.answerSubmitted) return
        moveToNextQuestion()
    }

    private fun moveToNextQuestion() {
        val state = _uiState.value
        val isLastQuestion = state.currentIndex >= state.questions.lastIndex
        if (isLastQuestion) {
            finishQuiz()
            return
        }

        _uiState.value = state.copy(
            currentIndex = state.currentIndex + 1,
            selectedAnswer = null,
            answerSubmitted = false,
            aiInsight = null,
            isAiLoading = false
        )
    }

    fun askAi() {
        val question = _uiState.value.currentQuestion ?: return
        val result = answersByQuestionId[question.id] ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAiLoading = true)
            val aiResult = askAiForQuestionUseCase(result)
            _uiState.value = _uiState.value.copy(
                isAiLoading = false,
                aiInsight = aiResult.getOrElse { it.message ?: "Unable to fetch AI explanation." }
            )
        }
    }

    private fun finishQuiz() {
        val results = _uiState.value.questions.mapNotNull { question -> answersByQuestionId[question.id] }
        val summary = QuizSummary(
            difficultyLevel = difficultyLevel,
            totalQuestions = _uiState.value.questions.size,
            correctCount = results.count { it.isCorrect },
            wrongCount = results.count { !it.isCorrect && !it.isSkipped },
            skippedCount = results.count { it.isSkipped },
            elapsedTimeMillis = System.currentTimeMillis() - quizStartTimeMillis,
            results = results
        )
        saveQuizSummaryUseCase(summary)
        _uiState.value = _uiState.value.copy(isCompleted = true)
    }
}
