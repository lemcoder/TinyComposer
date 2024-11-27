package pl.lemanski.tc.domain.service.audio

import pl.lemanski.mikroSoundFont.MikroSoundFont
import pl.lemanski.mikroSoundFont.io.toByteArrayLittleEndian
import pl.lemanski.mikroSoundFont.midi.MidiSequencer
import pl.lemanski.mikroSoundFont.midi.MidiVoiceMessage
import pl.lemanski.mikroaudio.MikroAudio
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.soundFont.SoundFontHolder
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.utils.exception.ApplicationStateException

internal class AudioService(
    private val soundFontRepository: SoundFontRepository,
    private val audioMapper: AudioMapper
) {
    private val mikroAudio = MikroAudio()

    fun isSoundFontLoaded(): Boolean = soundFontRepository.currentSoundFont() != null

    fun currentSoundFont(): SoundFontHolder? = soundFontRepository.currentSoundFont()

    fun useSoundFont(soundFont: ByteArray) {
        soundFontRepository.setSoundFont("default", soundFont)
    }

    fun generateAudioData(chordBeats: List<ChordBeats>, sampleRate: Int, bpm: Int): FloatArray {
        val midiMessages = listOf(
            MidiVoiceMessage.ProgramChange(0, 0, 0),
            *audioMapper.mapChordBeatsToMidiMessage(chordBeats, bpm).toTypedArray()
        )

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
        mikroAudio.playback(data.toByteArrayLittleEndian())
    }

    fun stopAudio() {
        mikroAudio.stopPlayback()
    }
}