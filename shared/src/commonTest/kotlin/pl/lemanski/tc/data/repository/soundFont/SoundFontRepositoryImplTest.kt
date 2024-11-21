package pl.lemanski.tc.data.repository.soundFont

import kotlin.test.*
import pl.lemanski.tc.data.cache.MemoryCache
import pl.lemanski.tc.utils.exception.EntryNotFoundException

class SoundFontRepositoryImplTest {
    private val memoryCache = MemoryCache()
    private val repository = SoundFontRepositoryImpl(memoryCache)

    @Test
    fun load_sound_font_throws_exception_if_file_does_not_exist() {
        val nonExistentPath = "/nonexistent/file.sf2"
        val exception = assertFailsWith<EntryNotFoundException> {
            repository.loadSoundFont(nonExistentPath)
        }
        assertEquals("SoundFont file not found", exception.message)
    }

    @Test
    fun set_sound_font_stores_in_memory_cache_and_sets_last_sound_font_name() {
        val soundFontName = "TestSoundFont"
        val soundFontData = byteArrayOf(1, 2, 3, 4, 5)

        repository.setSoundFont(soundFontName, soundFontData)

        val cachedData: ByteArray? = memoryCache.get(soundFontName)
        assertNotNull(cachedData)
        assertTrue(soundFontData contentEquals cachedData)
        assertEquals(soundFontName, repository.lastSoundFontName)
    }

    @Test
    fun current_sound_font_returns_sound_font_holder_when_sound_font_is_set() {
        val soundFontName = "ActiveSoundFont"
        val soundFontData = byteArrayOf(10, 20, 30, 40)

        repository.setSoundFont(soundFontName, soundFontData)

        val holder = repository.currentSoundFont()
        assertNotNull(holder)
        assertEquals(soundFontName, holder.name)
        assertTrue(soundFontData contentEquals holder.soundFont)
    }

    @Test
    fun current_sound_font_returns_null_when_no_sound_font_is_set() {
        val holder = repository.currentSoundFont()
        assertNull(holder)
    }

    @Test
    fun current_sound_font_returns_null_if_sound_font_is_not_in_cache() {
        repository.lastSoundFontName = "MissingSoundFont"
        val holder = repository.currentSoundFont()
        assertNull(holder)
    }
}
