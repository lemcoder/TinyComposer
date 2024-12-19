package pl.lemanski.tc.domain.repository.soundFont

import io.github.lemcoder.mikrosoundfont.SoundFont

interface SoundFontRepository {
    fun setSoundFont(name: String, soundFont: ByteArray)
    fun currentSoundFont(): SoundFont?
    fun getSoundFontPresets(): Map<Int, String>  // TODO create some structure for preset (maybe in library?)
}