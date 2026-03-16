package com.example.ouraiapp.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ouraiapp.domain.model.QuestionResult
import com.example.ouraiapp.domain.model.QuizSummary
import com.example.ouraiapp.domain.usecase.AskAiForQuestionUseCase
import com.example.ouraiapp.domain.usecase.ObserveLatestQuizSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ReviewUiState(
    val summary: QuizSummary? = null,
    val aiResponses: Map<Int, String> = emptyMap(),
    val loadingQuestionIds: Set<Int> = emptySet()
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    observeLatestQuizSummaryUseCase: ObserveLatestQuizSummaryUseCase,
    private val askAiForQuestionUseCase: AskAiForQuestionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    init {
        observeLatestQuizSummaryUseCase()
            .onEach { summary ->
                _uiState.value = _uiState.value.copy(summary = summary)
            }
            .launchIn(viewModelScope)
    }

    fun askAi(result: QuestionResult) {
        if (_uiState.value.loadingQuestionIds.contains(result.question.id)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loadingQuestionIds = _uiState.value.loadingQuestionIds + result.question.id
            )
            val response = askAiForQuestionUseCase(result)
                .getOrElse { it.message ?: "Unable to fetch AI response." }
            _uiState.value = _uiState.value.copy(
                aiResponses = _uiState.value.aiResponses + (result.question.id to response),
                loadingQuestionIds = _uiState.value.loadingQuestionIds - result.question.id
            )
        }
    }
}
