package com.example.ouraiapp.data.repository

import com.example.ouraiapp.BuildConfig
import com.example.ouraiapp.domain.model.QuestionResult
import com.example.ouraiapp.domain.repository.AiRepository
import com.example.ouraiapp.network.AiApiService
import com.example.ouraiapp.network.ChatCompletionRequest
import com.example.ouraiapp.network.ChatMessage
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val aiApiService: AiApiService
) : AiRepository {

    override suspend fun askQuestionHelp(result: QuestionResult): Result<String> {
        if (BuildConfig.OPENAI_API_KEY.isBlank()) {
            return Result.success(
                "AI is not configured yet.\n\nAdd OPENAI_API_KEY in your Gradle properties to enable live explanations.\n\nExpected sections:\n1. Simple explanation\n2. Interview tip\n3. Short code example"
            )
        }

        return runCatching {
            val request = ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    ChatMessage(
                        role = "system",
                        content = "You explain Android interview answers for developers. Respond with three short sections titled Simple explanation, Interview tip, and Code example."
                    ),
                    ChatMessage(
                        role = "user",
                        content = buildPrompt(result)
                    )
                )
            )
            aiApiService.createChatCompletion(request).choices.firstOrNull()?.message?.content
                ?: "No AI response received."
        }
    }

    private fun buildPrompt(result: QuestionResult): String {
        val correctOption = result.question.options.first { it.id == result.question.correctAnswer }
        return """
            Explain this Android interview question simply and give a real interview tip.

            Question:
            ${result.question.question}

            Correct answer:
            ${correctOption.text}

            Existing explanation:
            ${result.question.explanation}

            Include a concise Kotlin or Android code example when relevant.
        """.trimIndent()
    }
}
