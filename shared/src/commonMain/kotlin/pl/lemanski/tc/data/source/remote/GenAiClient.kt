package pl.lemanski.tc.data.source.remote

/**
 * Interface for interacting with a Generative AI client.
 */
internal interface GenAiClient {

    /**
     * Sets up the Generative AI client with the given context.
     *
     * @param context The context to be used for setup.
     */
    fun setup(context: String)

    /**
     * Generates a response based on the given prompt.
     *
     * @param prompt The prompt to generate a response for.
     * @return The generated response as a String.
     */
    suspend fun generate(prompt: String): String
}