package pl.lemanski.tc.data.repository.nlp

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import pl.lemanski.tc.data.source.remote.GenAiClient
import kotlin.test.Test
import kotlin.test.assertEquals

class NlpRepositoryTest {

    private val genAiApi = object : GenAiClient {
        override fun setup(context: String) {
            // Mock setup
        }

        override suspend fun generate(prompt: String): String {
            return when (prompt) {
                "chord prompt"     -> """[{"chord":{"type":"MAJOR","baseNote":{"value":60,"velocity":127}},"beats":4},{"chord":{"type":"MINOR","baseNote":{"value":62,"velocity":60}},"beats":2}]"""
                "chord prompt ns"  -> """  
                    [
                        {"chord":{"type":"MAJOR","baseNote":{"value":60,"velocity":127}},"beats":4},
                        {"chord":{"type":"MINOR","baseNote":{"value":62,"velocity":60}},"beats":2}
                    ]
                    """.trimIndent()
                "melody prompt"    -> """[{"note":{"value":60,"velocity":127},"beats":4},{"note":{"value":65,"velocity":60},"beats":4}]"""
                "melody prompt ns" -> """
                    [
                        {"note":{"value":60,"velocity":127},"beats":4},
                        {"note":{"value":65,"velocity":60},"beats":4}
                    ]
                    """.trimIndent()
                else               -> ""
            }
        }
    }


    private val TestScope.repository
        get() = NlpRepositoryImpl(
            genAiApi = genAiApi,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

    @Test
    fun testGenerateChordBeats() = runTest {
        val prompt = "chord prompt"
        val result = repository.generateChordBeats(prompt)
        assertEquals(2, result.size)
        assertEquals(60, result[0].first.notes[0].value)
        assertEquals(127, result[0].first.velocity)
        assertEquals(4, result[0].second)
        assertEquals(62, result[1].first.notes[0].value)
        assertEquals(60, result[1].first.velocity)
        assertEquals(2, result[1].second)
    }

    @Test
    fun testGenerateMelody() = runTest {
        val prompt = "melody prompt"
        val result = repository.generateMelody(prompt)
        assertEquals(2, result.size)
        assertEquals(60, result[0].first.value)
        assertEquals(127, result[0].first.velocity)
        assertEquals(4, result[0].second)
        assertEquals(65, result[1].first.value)
        assertEquals(60, result[1].first.velocity)
        assertEquals(4, result[1].second)
    }

    @Test
    fun testGenerateChordBeatsWithNewlinesAndSpaces() = runTest {
        val prompt = "chord prompt ns"
        val result = repository.generateChordBeats(prompt)
        assertEquals(2, result.size)
        assertEquals(60, result[0].first.notes[0].value)
        assertEquals(127, result[0].first.velocity)
        assertEquals(4, result[0].second)
        assertEquals(62, result[1].first.notes[0].value)
        assertEquals(60, result[1].first.velocity)
        assertEquals(2, result[1].second)
    }

    @Test
    fun testGenerateMelodyWithNewlinesAndSpaces() = runTest {
        val prompt = "melody prompt ns"
        val result = repository.generateMelody(prompt)
        assertEquals(2, result.size)
        assertEquals(60, result[0].first.value)
        assertEquals(127, result[0].first.velocity)
        assertEquals(4, result[0].second)
        assertEquals(65, result[1].first.value)
        assertEquals(60, result[1].first.velocity)
        assertEquals(4, result[1].second)
    }
}