package com.github.rr97.emotionalintelligence

import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.client.OpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import kotlinx.coroutines.runBlocking
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import kotlinx.coroutines.flow.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MessageGenerator {
    private val apiKey: String = System.getenv("OPENAI_API_KEY")
    private val openAI = OpenAI(token = apiKey, logging = LoggingConfig(LogLevel.None))

    suspend fun wordsOfAffirmation(mood: MoodType, sourceCode: String?): String {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-4-1106-preview"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = "You are a helpful assistant that encourages programmers based on their commits."
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = "A programmer just committed a change to the git repository. " +
                            "Provide words of affirmation based on the commit message written by them.\n" +
                            "DO NOT just tell them what they just wrote!\n" +
                            "DO instead tell them an uplifting joke related to what they just did.\n" +
                            "Write a maximum of 20 words!\n" +
                            "commit message: \"$sourceCode\"",
                )
            ),
            presencePenalty = 0.8,
            frequencyPenalty = .3,
            temperature =  0.8,
            maxTokens = 50,
            seed = 342582432,
        )
        val text = openAI.chatCompletions(chatCompletionRequest)
                .map { it.choices.first().delta.content.orEmpty() }
                .onEach { print(it) }
                .reduce { acc, string -> acc + string }
        return text;
    }
}