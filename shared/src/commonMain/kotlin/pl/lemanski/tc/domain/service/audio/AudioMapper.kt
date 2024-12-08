package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiVoiceMessage
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats

internal class AudioMapper {
    fun mapChordBeatsToMidiMessage(chordBeats: List<ChordBeats>, tempo: Int, channel: Int): List<MidiMessage> {
        var currentTime = 0
        val messages = mutableListOf<MidiMessage>()

        for (chordBeat in chordBeats) {
            val (chord, beats) = chordBeat

            val midiNoteOnMessages = chord.notes.map { note ->
                MidiVoiceMessage.NoteOn(currentTime, channel, note.value, chord.velocity)
            }
            messages.addAll(midiNoteOnMessages)

            val durationMillis = (beats * (60_000 / tempo))
            currentTime += durationMillis

            val midiNoteOffMessages = chord.notes.map { note ->
                MidiVoiceMessage.NoteOff(currentTime - 100, channel, note.value, chord.velocity)
            }

            messages.addAll(midiNoteOffMessages)
        }

        return messages
    }

    fun mapNoteBeatsToMidiMessage(noteBeats: List<NoteBeats>, tempo: Int, channel: Int): List<MidiMessage> {
        var currentTime = 0
        val messages = mutableListOf<MidiMessage>()

        for (noteBeat in noteBeats) {
            val (note, beats) = noteBeat

            val midiNoteOnMessage = MidiVoiceMessage.NoteOn(currentTime, channel, note.value, note.velocity)
            messages.add(midiNoteOnMessage)

            val durationMillis = (beats * (60_000 / tempo))
            currentTime += durationMillis

            val midiNoteOffMessage = MidiVoiceMessage.NoteOff(currentTime, channel, note.value, note.velocity)

            messages.add(midiNoteOffMessage)
        }

        return messages
    }
}