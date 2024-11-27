package pl.lemanski.tc.domain.service.audio

import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiVoiceMessage
import pl.lemanski.tc.domain.model.project.ChordBeats

internal class AudioMapper {
    fun mapChordBeatsToMidiMessage(chordBeats: List<ChordBeats>, tempo: Int): List<MidiMessage> {
        var currentTime = 0
        val messages = mutableListOf<MidiMessage>()

        for (chordBeat in chordBeats) {
            val (chord, beats) = chordBeat

            val midiNoteOnMessages = chord.notes.map { note ->
                MidiVoiceMessage.NoteOn(currentTime, 0, note.value, 127)
            }
            messages.addAll(midiNoteOnMessages)

            val durationMillis = (beats * 60_000 / tempo)
            currentTime += durationMillis

            val midiNoteOffMessages = chord.notes.map { note ->
                MidiVoiceMessage.NoteOff(currentTime, 0, note.value, 0)
            }

            messages.addAll(midiNoteOffMessages)
        }

        return messages
    }
}