package com.example.ouraiapp.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ouraiapp.domain.usecase.ObserveOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class MainUiState(
    val isLoading: Boolean = true,
    val onboardingCompleted: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    observeOnboardingCompletedUseCase: ObserveOnboardingCompletedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        observeOnboardingCompletedUseCase()
            .onEach { completed ->
                _uiState.value = MainUiState(
                    isLoading = false,
                    onboardingCompleted = completed
                )
            }
            .launchIn(viewModelScope)
    }
}
