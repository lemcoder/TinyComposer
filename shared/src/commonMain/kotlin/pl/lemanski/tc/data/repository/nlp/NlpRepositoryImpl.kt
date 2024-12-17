package pl.lemanski.tc.data.repository.nlp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import pl.lemanski.tc.data.model.core.ChordBeatsEntity
import pl.lemanski.tc.data.model.core.NoteBeatsEntity
import pl.lemanski.tc.data.model.core.toDomain
import pl.lemanski.tc.data.source.remote.GenAiClient
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.repository.nlp.NlpRepository
import pl.lemanski.tc.utils.Logger

internal class NlpRepositoryImpl(
    private val genAiApi: GenAiClient,
    private val dispatcher: CoroutineDispatcher,
) : NlpRepository {
    private val logger: Logger = Logger(this::class)

    override suspend fun generateChordBeats(prompt: String): List<ChordBeats> = withContext(dispatcher) {
        val context = """
            You a music composer. You composing song.
            Provide melody. The notes are in the JSON format:
            [{"chord":{"type":"MAJOR","baseNote":{"value":60,"velocity":127}},"beats":4},{"chord":{"type":"MINOR","baseNote":{"value":62,"velocity":60}},"beats":2}]
            The "type" is the chord type, the "baseNote" is the MIDI note that is the root of a chord (it has value of 1-127 where 60 is C5 and velocity (volume level) of 1-127), the "beats" is the number of measures that chord should last.
            available chord types are:
            MINOR, MAJOR, DIMINISHED, AUGMENTED, MAJOR_SEVENTH, MINOR_SEVENTH, DOMINANT_SEVENTH, HALF_DIMINISHED_SEVENTH, DIMINISHED_SEVENTH, AUGMENTED_SEVENTH, MINOR_SIXTH, MAJOR_SIXTH
        """.trimIndent()

        genAiApi.setup(context)
        val response = genAiApi.generate(prompt).replace("\n", "").trim()

        logger.debug("Response: $response")

        try {
            val notes = Json.decodeFromString<List<ChordBeatsEntity>>(response)
            return@withContext notes.map { it.toDomain() }
        } catch (e: Exception) {
            logger.error("Failed to decode note beats", e)
            throw e
        }
    }

    override suspend fun generateMelody(prompt: String): List<NoteBeats> = withContext(dispatcher) {
        val context = """
            You a music composer. You composing song.
            Provide melody. The notes are in the JSON format:
            [{"note":{"value":60,"velocity":127},"beats":4},{"note":{"value":65,"velocity":60},"beats":4}]
            The "value" is the MIDI note (1-127 where 60 is C5), the "velocity" is volume level and the beats is the number of beats that note should last.
        """.trimIndent()

        genAiApi.setup(context)
        val response = genAiApi.generate(prompt).replace("\n", "").trim()

        logger.debug("Response: $response")

        try {
            val notes = Json.decodeFromString<List<NoteBeatsEntity>>(response)
            return@withContext notes.map { it.toDomain() }
        } catch (e: Exception) {
            logger.error("Failed to decode note beats", e)
            throw e
        }
    }
}