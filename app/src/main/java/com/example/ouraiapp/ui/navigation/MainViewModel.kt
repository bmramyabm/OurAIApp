package com.example.ouraiapp.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ouraiapp.domain.usecase.ObserveOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MainUiState(
    val isLoading: Boolean = true,
    val onboardingCompleted: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    observeOnboardingCompletedUseCase: ObserveOnboardingCompletedUseCase
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = observeOnboardingCompletedUseCase()
        .map { onboardingCompleted ->
            MainUiState(
                isLoading = false,
                onboardingCompleted = onboardingCompleted
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUiState()
        )
}
