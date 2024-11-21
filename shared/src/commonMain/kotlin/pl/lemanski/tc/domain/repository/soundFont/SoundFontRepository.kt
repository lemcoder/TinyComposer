package pl.lemanski.tc.domain.repository.soundFont

import pl.lemanski.tc.domain.model.soundFont.SoundFontHolder

interface SoundFontRepository {

    fun loadSoundFont(path: String): ByteArray
    fun setSoundFont(name: String, soundFont: ByteArray)
    fun currentSoundFont(): SoundFontHolder?
}