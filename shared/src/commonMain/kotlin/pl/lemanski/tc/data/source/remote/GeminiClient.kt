package pl.lemanski.tc.data.source.remote

import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import dev.shreyaspatil.ai.client.generativeai.type.GenerationConfig
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import tinycomposer.shared.generated.resources.Res

internal class GeminiClient : GenAiClient {
    private var generativeModel: GenerativeModel? = null

    @OptIn(ExperimentalResourceApi::class)
    private val apiKey: String by lazy {
        runBlocking {
            Res.readBytes("files/gemini_api_key").decodeToString().trim()
        }
    }

    private fun generateContent(prompt: String): Flow<GenerateContentResponse> {
        return generativeModel?.generateContentStream(prompt) ?: throw IllegalStateException("Generative model not initialized.")
    }

    override fun setup(context: String) {
        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey,
            systemInstruction = content {
                text(context)
            },
            generationConfig = GenerationConfig.builder().apply {
                responseMimeType = "application/json"
            }.build()
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
