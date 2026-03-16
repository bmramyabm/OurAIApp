package com.example.ouraiapp.domain.repository

import com.example.ouraiapp.domain.model.QuestionResult

interface AiRepository {
    suspend fun askQuestionHelp(result: QuestionResult): Result<String>
}
