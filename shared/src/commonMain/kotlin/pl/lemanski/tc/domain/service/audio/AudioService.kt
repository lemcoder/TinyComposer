package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.SoundFont
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiSequencer
import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.utils.exception.ApplicationStateException

internal class AudioService(
    private val soundFontRepository: SoundFontRepository,
) {
    fun generateAudioData(midiMessages: List<MidiMessage>, sampleRate: Int): AudioStream {
        val soundFont = soundFontRepository.currentSoundFont() ?: throw ApplicationStateException("SoundFont not loaded")

        soundFont.setOutput(
            outputMode = SoundFont.OutputMode.TSF_MONO,
            sampleRate = AudioStream.SAMPLE_RATE,
            globalGainDb = 1.0f
        )

        val sequencer  = MidiSequencer(
            soundFont = soundFont,
            sampleRate = sampleRate,
            channels = 1
        )

        sequencer.loadMidiEvents(midiMessages)

        return AudioStream(
            data = sequencer.generate(),
            output = AudioStream.Output.MONO
        )
    }
}