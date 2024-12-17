package pl.lemanski.tc.data.repository.preset

import pl.lemanski.tc.data.source.cache.MemoryCache
import pl.lemanski.tc.utils.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class PresetRepositoryImplTest {

    private val memoryCache = MemoryCache()

    private val repository = PresetRepositoryImpl(memoryCache)

    @Test
    fun testGetChordsPreset() {
        val projectId = UUID.random()
        assertEquals(0, repository.getChordsPreset(projectId))

        memoryCache.put(projectId.toString() + "chord_preset", 5)
        assertEquals(5, repository.getChordsPreset(projectId))
    }

    @Test
    fun testGetMelodyPreset() {
        val projectId = UUID.random()
        assertEquals(0, repository.getMelodyPreset(projectId))

        memoryCache.put(projectId.toString() + "melody_preset", 7)
        assertEquals(7, repository.getMelodyPreset(projectId))
    }

    @Test
    fun testSetChordsPreset() {
        val projectId = UUID.random()
        repository.setChordsPreset(projectId, 3)
        assertEquals(3, memoryCache.get<Int>(projectId.toString() + "chord_preset"))
    }

    @Test
    fun testSetMelodyPreset() {
        val projectId = UUID.random()
        repository.setMelodyPreset(projectId, 4)
        assertEquals(4, memoryCache.get<Int>(projectId.toString() + "melody_preset"))
    }
}