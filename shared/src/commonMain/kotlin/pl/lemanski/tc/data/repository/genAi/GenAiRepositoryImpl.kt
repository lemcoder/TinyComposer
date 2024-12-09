package pl.lemanski.tc.data.repository.genAi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.lemanski.tc.data.persistent.decoder.tryDecodeChordBeats
import pl.lemanski.tc.data.persistent.decoder.tryDecodeNoteBeats
import pl.lemanski.tc.data.remote.genAi.client.GenAiClient
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats
import pl.lemanski.tc.domain.repository.genAi.GenAiRepository
import pl.lemanski.tc.utils.Logger

internal class GenAiRepositoryImpl(
    private val genAiApi: GenAiClient,
    private val dispatcher: CoroutineDispatcher,
) : GenAiRepository {
    private val logger: Logger = Logger(this::class)

    override suspend fun generateChordBeats(prompt: String): List<ChordBeats> = withContext(dispatcher) {
        val context = """
            You a music composer. You composing song.
            Provide chords. The chords are in the format of 12.minV60:1;15.maj7V60:3.
            The first number is the base note (MIDI value 60 for C5), the second is the chord type, the third is the velocity.
            The fourth is the number of beats that chord should last. Return the chords in the same format no newlines or spaces.
            Do not put trailing ; at the end of the string.
            Available chord types are:
            MINOR("min"),
            MAJOR("maj"),
            DIMINISHED("dim"),
            AUGMENTED("aug"),
            MAJOR_SEVENTH("maj7"),
            MINOR_SEVENTH("min7"),
            DOMINANT_SEVENTH("7"),
            HALF_DIMINISHED_SEVENTH("o7"),
            DIMINISHED_SEVENTH("dim7"),
            AUGMENTED_SEVENTH("aug7"),
            MINOR_SIXTH("min6"),
            MAJOR_SIXTH("maj6"),
        """.trimIndent()

        genAiApi.setup(context)
        val response = genAiApi.generate(prompt).replace("\n", "")

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
        val context = """
            You a music composer. You composing song.
            Provide melody. The notes are in the format of 52V60:2;44V70:4.
            The first number is the MIDI note, the second is the velocity.
            The third is the number of beats that note should last. Return the note in the same format no newlines or spaces.
            Do not put trailing ; at the end of the string.
        """.trimIndent()

        genAiApi.setup(context)
        val response = genAiApi.generate(prompt).replace("\n", "")

        logger.debug("Response: $response")

        try {
            val notes = response.tryDecodeNoteBeats()
            return@withContext notes
        } catch (e: Exception) {
            logger.error("Failed to decode note beats", e)
            throw e
        }
    }
}