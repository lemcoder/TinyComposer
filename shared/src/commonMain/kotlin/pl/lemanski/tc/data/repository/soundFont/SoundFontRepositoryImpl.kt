package pl.lemanski.tc.data.repository.soundFont

import io.github.lemcoder.mikrosoundfont.MikroSoundFont
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import pl.lemanski.tc.data.cache.MemoryCache
import pl.lemanski.tc.domain.model.soundFont.SoundFontHolder
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.utils.exception.EntryNotFoundException

internal class SoundFontRepositoryImpl(
    private val cacheMemory: MemoryCache
) : SoundFontRepository {
    internal var lastSoundFontName: String? = null

    override fun loadSoundFont(path: String): ByteArray {
        val filePath = Path(path)
        if (!SystemFileSystem.exists(filePath)) {
            throw EntryNotFoundException("SoundFont file not found")
        }

        return SystemFileSystem.source(Path(path)).buffered().readByteArray()
    }

    override fun setSoundFont(name: String, soundFont: ByteArray) {
        cacheMemory.remove(name)
        lastSoundFontName = name
        cacheMemory.put(name, soundFont)
    }

    override fun currentSoundFont(): SoundFontHolder? {
        val ln = lastSoundFontName ?: return null
        val soundFont = cacheMemory.get<ByteArray>(ln) ?: return null

        return SoundFontHolder(
            name = ln,
            soundFont = soundFont
        )
    }

    override fun getSoundFontPresets(): List<Pair<Int, String>> {
        val soundFont = currentSoundFont()?.soundFont ?: return emptyList()

        // FIXME do not load here
        MikroSoundFont.load(soundFont).let { sf ->
            return (0 until sf.getPresetsCount()).map { index ->
                index to sf.getPresetName(index)
            }
        }
    }
}