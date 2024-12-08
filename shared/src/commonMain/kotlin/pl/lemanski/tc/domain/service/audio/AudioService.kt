package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.MikroSoundFont
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiSequencer
import io.github.lemcoder.mikrosoundfont.midi.MidiVoiceMessage
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.soundFont.SoundFontHolder
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.utils.exception.ApplicationStateException

internal class AudioService(
    private val soundFontRepository: SoundFontRepository,
    private val audioMapper: AudioMapper
) {
    fun isSoundFontLoaded(): Boolean = soundFontRepository.currentSoundFont() != null

    fun currentSoundFont(): SoundFontHolder? = soundFontRepository.currentSoundFont()

    fun useSoundFont(soundFont: ByteArray) {
        soundFontRepository.setSoundFont("default", soundFont)
    }

    fun generateAudioData(midiMessages: List<MidiMessage>, sampleRate: Int, bpm: Int): FloatArray {
        if (!isSoundFontLoaded()) {
            throw ApplicationStateException("SoundFont not loaded")
        }

        val holder = soundFontRepository.currentSoundFont()!!
        val soundFont = MikroSoundFont.load(holder.soundFont)

        return MidiSequencer(soundFont, sampleRate).apply {
            loadMidiEvents(midiMessages)
        }.generate()
    }

    //---


}

internal expect suspend fun playAudio(data: FloatArray, sampleRate: Int)