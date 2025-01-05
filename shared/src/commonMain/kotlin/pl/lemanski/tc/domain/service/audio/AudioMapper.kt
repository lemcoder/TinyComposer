package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiVoiceMessage
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.model.project.CompingStyle

internal class AudioMapper {

    fun mapChordBeatsToMidiMessage(chordBeats: List<ChordBeats>, tempo: Int, channel: Int, compingStyle: CompingStyle) = when (compingStyle) {
        CompingStyle.STRAIGHT   -> mapChordBeatsToMidiMessageStraight(chordBeats, tempo, channel)
        CompingStyle.CHARLESTON -> mapChordBeatsToMidiMessageCharleston(chordBeats, tempo, channel)
    }

    private fun mapChordBeatsToMidiMessageStraight(chordBeats: List<ChordBeats>, tempo: Int, channel: Int): List<MidiMessage> {
        var currentTime = 0
        val messages = mutableListOf<MidiMessage>()

        chordBeats.forEach { (chord, beats) ->
            messages.addAll(chord.notes.mapIndexed { index, note ->
                MidiVoiceMessage.NoteOn(currentTime + (index * 7), channel, note.value, chord.velocity)
            })

            currentTime += (beats * (60_000 / tempo))

            messages.addAll(chord.notes.map { note ->
                MidiVoiceMessage.NoteOff(currentTime - 80, channel, note.value, chord.velocity)
            })
        }

        return messages
    }

    // FIXME works only for 4 over 4 time signature
    // TODO figure out better way to handle this
    private fun mapChordBeatsToMidiMessageCharleston(chordBeats: List<ChordBeats>, tempo: Int, channel: Int): List<MidiMessage> {
        var currentTime = 0
        val messages = mutableListOf<MidiMessage>()

        chordBeats
            .filter { (_, beats) -> beats >= 4 }
            .forEach { (chord, _) ->
                messages.addAll(chord.notes.mapIndexed { index, note ->
                    MidiVoiceMessage.NoteOn(currentTime + (index * 7), channel, note.value, chord.velocity)
                })

                currentTime += ((60_000 / tempo) * 1.5f).toInt()

                messages.addAll(chord.notes.map { note ->
                    MidiVoiceMessage.NoteOff(currentTime - 160, channel, note.value, chord.velocity)
                })

                messages.addAll(chord.notes.mapIndexed { index, note ->
                    MidiVoiceMessage.NoteOn(currentTime, channel, note.value, chord.velocity)
                })

                currentTime += ((60_000 / tempo) * .25f).toInt()

                messages.addAll(chord.notes.map { note ->
                    MidiVoiceMessage.NoteOn(currentTime, channel, note.value, 0)
                })

                currentTime += ((60_000 / tempo) * 2f).toInt()

                messages.addAll(chord.notes.map { note ->
                    MidiVoiceMessage.NoteOff(currentTime - 80, channel, note.value, chord.velocity)
                })
            }

        return messages
    }

    // Note beats

    fun mapNoteBeatsToMidiMessage(noteBeats: List<NoteBeats>, tempo: Int, channel: Int): List<MidiMessage> {
        var currentTime = 0
        val messages = mutableListOf<MidiMessage>()

        noteBeats.forEach { (note, beats) ->
            messages.add(MidiVoiceMessage.NoteOn(currentTime, channel, note.value, note.velocity))

            currentTime += (beats * (60_000 / tempo))

            messages.add(MidiVoiceMessage.NoteOff(currentTime, channel, note.value, note.velocity))
        }

        return messages
    }
}