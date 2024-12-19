package pl.lemanski.tc.data.repository.soundFont

import io.github.lemcoder.mikrosoundfont.SoundFont
import io.github.lemcoder.mikrosoundfont.SoundFontLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.lemanski.tc.data.source.cache.MemoryCache
import pl.lemanski.tc.data.source.persistent.ResourcesLoader
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.utils.Logger

internal class SoundFontRepositoryImpl(
    private val cacheMemory: MemoryCache,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val soundFontLoader: SoundFontLoader,
    private val resourcesLoader: ResourcesLoader
) : SoundFontRepository {

    private val logging = Logger(this::class)
    private var lastSfName: String = DEFAULT_SOUNDFONT_NAME

    init {
        // Have to load default soundfont, it's needed for the app to work
        CoroutineScope(coroutineDispatcher).launch {
            resourcesLoader.loadBytes(DEFAULT_SOUNDFONT_PATH).let {
                val soundFont = soundFontLoader.load(it)
                cacheMemory.put(DEFAULT_SOUNDFONT_NAME, soundFont)
            }
        }.invokeOnCompletion {
            logging.info("Default soundfont loaded form $DEFAULT_SOUNDFONT_PATH")
        }
    }

    override fun setSoundFont(name: String, soundFont: ByteArray) {
        cacheMemory.remove(name)
        val newSoundFont = soundFontLoader.load(soundFont)
        lastSfName = name
        cacheMemory.put(name, newSoundFont)
    }

    override fun currentSoundFont(): SoundFont? {
        return cacheMemory.get<SoundFont>(lastSfName)
    }

    override fun getSoundFontPresets(): Map<Int, String> {
        val soundFont = currentSoundFont() ?: return emptyMap()

        return (0 until soundFont.getPresetsCount()).map { index ->
            index to soundFont.getPresetName(index)
        }.toMap()
    }

    companion object {
        const val DEFAULT_SOUNDFONT_NAME = "default_soundfont"
        const val DEFAULT_SOUNDFONT_PATH = "files/font.sf2"
    }
}