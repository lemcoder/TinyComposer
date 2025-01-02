package pl.lemanski.tc.domain.useCase.generateAudio

import io.github.lemcoder.mikrosoundfont.midi.MidiMetaMessage
import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.model.project.CompingStyle
import pl.lemanski.tc.domain.service.audio.AudioMapper
import pl.lemanski.tc.domain.service.audio.AudioService

internal class GenerateAudioUseCaseImpl(
    private val audioService: AudioService,
    private val audioMapper: AudioMapper
) : GenerateAudioUseCase {

    override suspend operator fun invoke(
        errorHandler: GenerateAudioUseCase.ErrorHandler,
        chordBeats: List<ChordBeats>,
        chordsPreset: Int,
        noteBeats: List<NoteBeats>,
        notesPreset: Int,
        tempo: Int,
        compingStyle: CompingStyle
    ): AudioStream {
        val tempoMessage = MidiMetaMessage.SetTempo(0, tempo)

        val chordMidiMessages = try {
            listOf(*audioMapper.mapChordBeatsToMidiMessage(chordBeats, tempo, chordsPreset, compingStyle).toTypedArray())
        } catch (ex: Exception) {
            errorHandler.onInvalidChordBeats()
            return AudioStream.EMPTY
        }

        val noteMidiMessages = try {
            listOf(
                *audioMapper.mapNoteBeatsToMidiMessage(noteBeats, tempo, notesPreset).toTypedArray()
            )
        } catch (ex: Exception) {
            errorHandler.onInvalidNoteBeats()
            return AudioStream.EMPTY
        }

        val midiMessages = (listOf(tempoMessage) + chordMidiMessages + noteMidiMessages).sortedBy { it.time }

        return audioService.generateAudioData(midiMessages, 44_100)
    }
}