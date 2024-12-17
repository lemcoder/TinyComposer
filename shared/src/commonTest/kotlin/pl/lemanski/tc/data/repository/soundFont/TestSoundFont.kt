package pl.lemanski.tc.data.repository.soundFont

import io.github.lemcoder.mikrosoundfont.Channel
import io.github.lemcoder.mikrosoundfont.SoundFont

internal class TestSoundFont : SoundFont {
    override val channels: List<Channel> = emptyList()

    override fun activeVoiceCount(): Int {
        return 0
    }

    override fun bankGetPresetName(bank: Int, presetNumber: Int): String {
        return "Preset $presetNumber"
    }

    override fun bankNoteOff(bank: Int, presetNumber: Int, key: Int) {
        // No-op for testing
    }

    override fun bankNoteOn(bank: Int, presetNumber: Int, key: Int, velocity: Float) {
        // No-op for testing
    }

    override fun getPresetIndex(bank: Int, presetNumber: Int): Int {
        return presetNumber
    }

    override fun getPresetName(presetIndex: Int): String {
        return "Preset $presetIndex"
    }

    override fun getPresetsCount(): Int {
        return 10 // Arbitrary number for testing
    }

    override fun noteOff(presetIndex: Int, key: Int) {
        // No-op for testing
    }

    override fun noteOffAll() {
        // No-op for testing
    }

    override fun noteOn(presetIndex: Int, key: Int, velocity: Float) {
        // No-op for testing
    }

    override fun renderFloat(samples: Int, channels: Int, isMixing: Boolean): FloatArray {
        return FloatArray(samples * channels) // Dummy data for testing
    }

    override fun reset() {
        // No-op for testing
    }

    override fun setBankPreset(channel: Int, bank: Int, presetNumber: Int) {
        // No-op for testing
    }

    override fun setMaxVoices(maxVoices: Int) {
        // No-op for testing
    }

    override fun setOutput(outputMode: SoundFont.OutputMode, sampleRate: Int, globalGainDb: Float) {
        // No-op for testing
    }

    override fun setVolume(globalGain: Float) {
        // No-op for testing
    }
}