package pl.lemanski.tc.data.repository.genAi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import pl.lemanski.tc.data.persistent.decoder.tryDecodeChordBeats
import pl.lemanski.tc.data.remote.genAi.client.GenAiClient
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats
import pl.lemanski.tc.domain.repository.genAi.GenAiRepository
import pl.lemanski.tc.utils.Logger

internal class GenAiRepositoryImpl(
    private val genAiApi: GenAiClient,
    private val dispatcher: CoroutineDispatcher,
) : GenAiRepository {
    private val context = "music"
    private val logger: Logger = Logger(this::class)

    override suspend fun generateChordBeats(prompt: String): List<ChordBeats> = withContext(dispatcher) {
        genAiApi.setContext(context)
        val response = genAiApi.generate(prompt)

        logger.debug("Response: $response")

        try {
            val chords = response.tryDecodeChordBeats()
            return@withContext chords
        } catch (e: Exception) {
            logger.error("Failed to decode chord beats", e)
            throw e
        }
    }

    override suspend fun generateMelody(prompt: String): List<NoteBeats> = withContext(dispatcher) {
        return@withContext emptyList<NoteBeats>() // TODO
    }
}