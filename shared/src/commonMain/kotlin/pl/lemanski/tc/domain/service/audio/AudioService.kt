package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.SoundFont
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiSequencer
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.utils.exception.ApplicationStateException

internal class AudioService(
    private val soundFontRepository: SoundFontRepository,
) {
    fun isSoundFontLoaded(): Boolean = soundFontRepository.currentSoundFont() != null

    fun useSoundFont(soundFont: ByteArray) {
        soundFontRepository.setSoundFont("default", soundFont)
    }

    fun generateAudioData(midiMessages: List<MidiMessage>, sampleRate: Int): FloatArray {
        if (!isSoundFontLoaded()) {
            throw ApplicationStateException("SoundFont not loaded")
        }

        val soundFont = soundFontRepository.currentSoundFont() ?: throw ApplicationStateException("SoundFont not loaded")
        soundFont.setOutput(SoundFont.OutputMode.TSF_MONO, sampleRate, 1.0f)

        return MidiSequencer(soundFont, sampleRate, 1).apply {
            loadMidiEvents(midiMessages)
        }.generate()
    }

    //---


}

internal expect suspend fun playAudio(data: FloatArray, sampleRate: Int)