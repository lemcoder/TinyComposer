package pl.lemanski.tc.domain.service.audio

import pl.lemanski.mikroSoundFont.MikroSoundFont
import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiSequencer
import pl.lemanski.tc.domain.model.soundFont.SoundFontHolder
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.utils.exception.ApplicationStateException

class AudioService(
    private val soundFontRepository: SoundFontRepository,
) {

    fun isSoundFontLoaded(): Boolean = soundFontRepository.currentSoundFont() != null

    fun currentSoundFont(): SoundFontHolder? = soundFontRepository.currentSoundFont()

    fun useNewSoundFont(path: String) {
        val soundFont = soundFontRepository.loadSoundFont(path)
        soundFontRepository.setSoundFont(path, soundFont)
    }

    fun generateAudioData(midiMessages: List<MidiMessage>, sampleRate: Int): FloatArray {
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

    fun playAudio(data: FloatArray, sampleRate: Int) {
        // TODO
    }

    fun stopAudio() {
        // TODO
    }
}