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

class AskAiForQuestionUseCase @Inject constructor(
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(result: QuestionResult): Result<String> = aiRepository.askQuestionHelp(result)
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
