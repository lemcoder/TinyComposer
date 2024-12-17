package pl.lemanski.tc.data.repository.soundFont

import io.github.lemcoder.mikrosoundfont.SoundFont
import io.github.lemcoder.mikrosoundfont.SoundFontLoader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import pl.lemanski.tc.data.source.cache.MemoryCache
import pl.lemanski.tc.data.source.persistent.ResourcesLoader
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SoundFontRepositoryTest {
    private val memoryCache = MemoryCache()
    private val soundFontLoader = object : SoundFontLoader {
        override fun load(data: ByteArray): SoundFont {
            return TestSoundFont()
        }
    }
    private val resourcesLoader = object : ResourcesLoader {
        override suspend fun loadBytes(path: String): ByteArray {
            return ByteArray(0)
        }
    }

    private val TestScope.repository: SoundFontRepository
        get() = SoundFontRepositoryImpl(
            cacheMemory = memoryCache,
            coroutineDispatcher = StandardTestDispatcher(testScheduler),
            soundFontLoader = soundFontLoader,
            resourcesLoader = resourcesLoader
        )

    @Test
    fun testSetSoundFont() = runTest {
        val soundFontData = ByteArray(0) // Mock sound font data
        repository.setSoundFont("test_soundfont", soundFontData)
        val soundFont = memoryCache.get<SoundFont>("test_soundfont")
        assertNotNull(soundFont)
    }

    @Test
    fun testCurrentSoundFont() = runTest {
        val repo = repository
        val soundFontData = ByteArray(0) // Mock sound font data
        repo.setSoundFont("test_soundfont", soundFontData)
        val soundFont = repo.currentSoundFont()
        assertNotNull(soundFont)
    }

    @Test
    fun testGetSoundFontPresets() = runTest {
        val repo = repository
        val soundFontData = ByteArray(0) // Mock sound font data
        repo.setSoundFont("test_soundfont", soundFontData)
        val presets = repo.getSoundFontPresets()
        assertEquals(10, presets.size) // Assuming the mock sound font has 10 presets
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDefaultSoundFontLoaded() = runTest {
        repository // Initialize repository
        advanceUntilIdle()
        val defaultSoundFont = memoryCache.get<SoundFont>(SoundFontRepositoryImpl.DEFAULT_SOUNDFONT_NAME)
        assertNotNull(defaultSoundFont)
    }
}