package com.example.ouraiapp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ouraiapp.domain.usecase.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val bullets: List<String>
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    val pages = listOf(
        OnboardingPage(
            title = "Welcome to Android Interview Prep",
            description = "Master Android concepts with guided quiz sessions and realistic interview-style questions.",
            bullets = listOf(
                "90+ carefully crafted questions",
                "Detailed explanations after every answer",
                "Track progress while you practice",
                "Review your mistakes after each quiz"
            )
        ),
        OnboardingPage(
            title = "Choose Your Difficulty Level",
            description = "Select the level that matches your current experience and target roles.",
            bullets = listOf(
                "Junior: Activities, views, intents, and basics",
                "Mid / Senior: MVVM, coroutines, Jetpack components",
                "Advanced: Performance, internals, Compose",
                "30+ Android interview topics"
            )
        ),
        OnboardingPage(
            title = "Flexible Quiz Modes",
            description = "Practice in a quick sprint or simulate a longer interview session whenever you have time.",
            bullets = listOf(
                "Quick Sprint: 10 questions",
                "Standard Quiz: 20 questions",
                "Full Mock Test: 30 questions",
                "Live score tracking and explanations"
            )
        ),
        OnboardingPage(
            title = "Learn From Every Answer",
            description = "Use explanations and AI-powered follow-ups to turn each question into a concept refresher.",
            bullets = listOf(
                "See correct solutions instantly",
                "Open a full review after the session",
                "Ask AI for simplified explanations",
                "Get interview tips and code examples"
            )
        )
    )

    fun completeOnboarding(onCompleted: () -> Unit) {
        viewModelScope.launch {
            completeOnboardingUseCase()
            onCompleted()
        }
    }
}
