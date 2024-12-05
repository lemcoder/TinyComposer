package pl.lemanski.tc.data.remote.genAi.client

import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import tinycomposer.shared.generated.resources.Res

internal class GeminiClient : GenAiClient {
    private var context: String = ""
    @OptIn(ExperimentalResourceApi::class)
    private val apiKey: String by lazy {
        runBlocking {
            Res.readBytes("files/gemini_api_key").decodeToString().trim()
        }
    }


    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )

    private fun generateContent(prompt: String): Flow<GenerateContentResponse> {
        return generativeModel.generateContentStream(prompt)
    }

    override fun setContext(context: String) {
        this.context = context
    }

    override suspend fun generate(prompt: String): String {
        val response = StringBuilder()

        generateContent(prompt).collect {
            response.append(it.text)
        }

        return response.toString()
    }
}
