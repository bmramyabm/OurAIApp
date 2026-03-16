package com.example.ouraiapp.data.repository

import com.example.ouraiapp.data.db.QuestionDao
import com.example.ouraiapp.domain.model.AnswerOption
import com.example.ouraiapp.domain.model.DifficultyLevel
import com.example.ouraiapp.domain.model.Question
import com.example.ouraiapp.domain.repository.InterviewRepository
import javax.inject.Inject

class InterviewRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
) : InterviewRepository {

    override suspend fun getQuizQuestions(difficultyLevel: DifficultyLevel, count: Int): List<Question> {
        return questionDao.getQuestionsForQuiz(difficultyLevel.value, count).map { entity ->
            Question(
                id = entity.id,
                topic = entity.topic,
                difficultyLevel = DifficultyLevel.fromValue(entity.difficultyLevel),
                question = entity.question,
                options = listOf(
                    AnswerOption("A", entity.optionA),
                    AnswerOption("B", entity.optionB),
                    AnswerOption("C", entity.optionC),
                    AnswerOption("D", entity.optionD)
                ),
                correctAnswer = entity.correctAnswer,
                explanation = entity.explanation
            )
        }
    }

    override suspend fun getQuestionCount(): Int = questionDao.countQuestions()
}
