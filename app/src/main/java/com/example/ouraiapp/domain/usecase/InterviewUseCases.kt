package com.example.ouraiapp.domain.usecase

import com.example.ouraiapp.domain.model.DifficultyLevel
import com.example.ouraiapp.domain.model.Question
import com.example.ouraiapp.domain.model.QuestionResult
import com.example.ouraiapp.domain.model.QuizSummary
import com.example.ouraiapp.domain.repository.AiRepository
import com.example.ouraiapp.domain.repository.InterviewRepository
import com.example.ouraiapp.domain.repository.PreferencesRepository
import com.example.ouraiapp.domain.repository.QuizSessionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetQuizQuestionsUseCase @Inject constructor(
    private val interviewRepository: InterviewRepository
) {
    suspend operator fun invoke(difficultyLevel: DifficultyLevel, count: Int): List<Question> {
        return interviewRepository.getQuizQuestions(difficultyLevel, count)
    }
}

class GetQuestionCountUseCase @Inject constructor(
    private val interviewRepository: InterviewRepository
) {
    suspend operator fun invoke(): Int = interviewRepository.getQuestionCount()
}

class ObserveOnboardingCompletedUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> = preferencesRepository.onboardingCompleted
}

class CompleteOnboardingUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke() = preferencesRepository.setOnboardingCompleted(true)
}

class ObserveAiExplanationUsageCountUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Int> = preferencesRepository.aiExplanationUsageCount
}

class AskAiForQuestionUseCase @Inject constructor(
    private val aiRepository: AiRepository,
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(result: QuestionResult): Result<String> {
        val usageCount = preferencesRepository.incrementAiExplanationUsage()
        if (usageCount > FREE_AI_EXPLANATION_LIMIT) {
            return Result.success(
                "Upgrade coming soon.\n\nYou have used your first 5 local AI explanations. A future premium option will unlock more explanations."
            )
        }
        return aiRepository.askQuestionHelp(result)
    }

    private companion object {
        const val FREE_AI_EXPLANATION_LIMIT = 5
    }
}

class SaveQuizSummaryUseCase @Inject constructor(
    private val quizSessionRepository: QuizSessionRepository
) {
    operator fun invoke(summary: QuizSummary) = quizSessionRepository.saveSummary(summary)
}

class ObserveLatestQuizSummaryUseCase @Inject constructor(
    private val quizSessionRepository: QuizSessionRepository
) {
    operator fun invoke(): Flow<QuizSummary?> = quizSessionRepository.latestSummary()
}
