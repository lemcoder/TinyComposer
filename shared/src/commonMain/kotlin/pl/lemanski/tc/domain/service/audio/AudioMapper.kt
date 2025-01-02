package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiVoiceMessage
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.model.project.CompingStyle

internal class AudioMapper {


    fun mapChordBeatsToMidiMessage(chordBeats: List<ChordBeats>, tempo: Int, channel: Int, compingStyle: CompingStyle): List<MidiMessage> {
        return when (compingStyle) {
            CompingStyle.STRAIGHT   -> mapChordBeatsToMidiMessageStraight(chordBeats, tempo, channel)
            CompingStyle.CHARLESTON -> mapChordBeatsToMidiMessageCharleston(chordBeats, tempo, channel)
        }
    }

    private fun mapChordBeatsToMidiMessageStraight(chordBeats: List<ChordBeats>, tempo: Int, channel: Int): List<MidiMessage> {
        var currentTime = 0
        val messages = mutableListOf<MidiMessage>()

        for (chordBeat in chordBeats) {
            val (chord, beats) = chordBeat

            val midiNoteOnMessages = chord.notes.mapIndexed { index, note ->
                // humanize the notes by adding a small delay between each note
                MidiVoiceMessage.NoteOn(currentTime + (index * 7), channel, note.value, chord.velocity)
            }
            messages.addAll(midiNoteOnMessages)

            val durationMillis = (beats * (60_000 / tempo))
            currentTime += durationMillis

            val midiNoteOffMessages = chord.notes.map { note ->
                // humanize the notes by adding a small delay between each chord
                MidiVoiceMessage.NoteOff(currentTime - 80, channel, note.value, chord.velocity)
            }

            messages.addAll(midiNoteOffMessages)
        }

        return messages
    }

    private fun mapChordBeatsToMidiMessageCharleston(chordBeats: List<ChordBeats>, tempo: Int, channel: Int): List<MidiMessage> {
        var currentTime = 0
        val messages = mutableListOf<MidiMessage>()

        for (chordBeat in chordBeats) {
            val (chord, beats) = chordBeat
            if (beats == 0) {
                continue
            }

            if (beats < 4) {
                // if the chord is shorter than a quarter note, play it straight
                // TODO this is a temporary solution, we need to implement a proper way to handle this
                val straightMessages = mapChordBeatsToMidiMessageStraight(listOf(chordBeat), tempo, channel).map {
                    when (it) {
                        is MidiVoiceMessage.NoteOn  -> it.copy(time = it.time + currentTime)
                        is MidiVoiceMessage.NoteOff -> it.copy(time = it.time + currentTime)
                        else                        -> it
                    }
                }

                messages.addAll(straightMessages)
                continue
            }

            val firstBeatNoteOnMessages = chord.notes.mapIndexed { index, note ->
                // humanize the notes by adding a small delay between each note
                MidiVoiceMessage.NoteOn(currentTime + (index * 7), channel, note.value, chord.velocity)
            }

            // handle the first beat of the charleston pattern
            messages.addAll(firstBeatNoteOnMessages)

            val firstDuration = ((60_000 / tempo) * 1.5f).toInt()
            currentTime += firstDuration

            val firstBeatNoteOffMessages = chord.notes.map { note ->
                // humanize the notes by adding a small delay between each chord
                MidiVoiceMessage.NoteOff(currentTime - 160, channel, note.value, chord.velocity)
            }

            messages.addAll(firstBeatNoteOffMessages)

            // handle the second beat of the charleston pattern

            val secondBeatNoteOnMessages = chord.notes.mapIndexed { index, note ->
                // humanize the notes by adding a small delay between each note
                MidiVoiceMessage.NoteOn(currentTime, channel, note.value, chord.velocity)
            }

            messages.addAll(secondBeatNoteOnMessages)

            val secondDuration = ((60_000 / tempo) * .25f).toInt()
            currentTime += secondDuration

            val secondBeatNoteOffMessages = chord.notes.map { note ->
                // humanize the notes by adding a small delay between each chord
                MidiVoiceMessage.NoteOn(currentTime, channel, note.value, 0)
            }

            messages.addAll(secondBeatNoteOffMessages)

            currentTime += ((60_000 / tempo) * 2f).toInt()
            val finalMessages = chord.notes.map { note ->
                // humanize the notes by adding a small delay between each chord
                MidiVoiceMessage.NoteOff(currentTime - 80, channel, note.value, chord.velocity)
            }

            messages.addAll(finalMessages)
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