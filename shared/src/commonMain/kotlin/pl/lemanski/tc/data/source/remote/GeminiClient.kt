package pl.lemanski.tc.data.source.remote

import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import dev.shreyaspatil.ai.client.generativeai.type.GenerationConfig
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow

internal class GeminiClient : GenAiClient {
    private var generativeModel: GenerativeModel? = null
    private val jellyFish: String by lazy {
        jelly() + fish()
    }

    private fun generateContent(prompt: String): Flow<GenerateContentResponse> {
        return generativeModel?.generateContentStream(prompt) ?: throw IllegalStateException("Generative model not initialized.")
    }

    override fun setup(context: String) {
        generativeModel = GenerativeModel(
            "gemini-1.5-flash",
            jellyFish,
            GenerationConfig.builder().apply {
                responseMimeType = "application/json"
            }.build(),
            systemInstruction = content {
                text(context)
            }
        )
    }

    override suspend fun generate(prompt: String): String {
        val response = StringBuilder()

        generateContent(prompt).collect {
            response.append(it.text)
        }

        return response.toString()
    }
}
