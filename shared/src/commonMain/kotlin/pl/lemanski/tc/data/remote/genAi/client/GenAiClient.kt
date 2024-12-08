package pl.lemanski.tc.data.remote.genAi.client

internal interface GenAiClient {
    fun setContext(context: String)

    suspend fun generate(prompt: String): String
}