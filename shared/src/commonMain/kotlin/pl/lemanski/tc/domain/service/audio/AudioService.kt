package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.SoundFont
import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiSequencer
import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.exception.ApplicationStateException

internal class AudioService(
    private val soundFontRepository: SoundFontRepository,
) {
    private val logger = Logger(this::class)

    fun generateAudioData(midiMessages: List<MidiMessage>, sampleRate: Int): AudioStream {
        try {
            logger.debug("Generating audio data")

            val soundFont = soundFontRepository.currentSoundFont() ?: throw ApplicationStateException("SoundFont not loaded")

            logger.debug("Current SF")

            logger.debug("Setting output")
            soundFont.setOutput(
                outputMode = SoundFont.OutputMode.TSF_MONO,
                sampleRate = AudioStream.SAMPLE_RATE,
                globalGainDb = 1.0f
            )

            logger.debug("Creating sequencer")
            val sequencer = MidiSequencer(
                soundFont = soundFont,
                sampleRate = sampleRate,
                channels = 1
            )

            logger.debug("Loading MIDI events")
            sequencer.loadMidiEvents(midiMessages)

            logger.debug("Generating audio data")
            return AudioStream(
                data = sequencer.generate(),
                output = AudioStream.Output.MONO
            )
        } catch (ex: Exception) {
            logger.error("Error generating audio data", ex)
            throw ex
        }
    }
}