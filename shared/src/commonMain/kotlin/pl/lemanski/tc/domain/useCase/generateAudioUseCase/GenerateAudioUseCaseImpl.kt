package pl.lemanski.tc.domain.useCase.generateAudioUseCase

import io.github.lemcoder.mikrosoundfont.midi.MidiMetaMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiVoiceMessage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats
import pl.lemanski.tc.domain.service.audio.AudioMapper
import pl.lemanski.tc.domain.service.audio.AudioService
import tinycomposer.shared.generated.resources.Res

internal class GenerateAudioUseCaseImpl(
    private val audioService: AudioService,
    private val audioMapper: AudioMapper
) : GenerateAudioUseCase {

    @OptIn(ExperimentalResourceApi::class)
    override suspend operator fun invoke(
        errorHandler: GenerateAudioUseCase.ErrorHandler,
        chordBeats: List<ChordBeats>,
        noteBeats: List<NoteBeats>,
        tempo: Int
    ): FloatArray {
        if (!audioService.isSoundFontLoaded()) {
            Res.readBytes("files/font.sf2").let {
                audioService.useSoundFont(it)
            }
        }

        val tempoMessage = MidiMetaMessage.SetTempo(0, tempo)

        val chordMidiMessages = try {
            listOf(
                MidiVoiceMessage.ProgramChange(0, 0, 0),
                *audioMapper.mapChordBeatsToMidiMessage(chordBeats, tempo, 0).toTypedArray()
            )
        } catch (ex: Exception) {
            errorHandler.onInvalidChordBeats()
            return floatArrayOf()
        }

        val noteMidiMessages = try {
            listOf(
                MidiVoiceMessage.ProgramChange(0, 1, 8),
                *audioMapper.mapNoteBeatsToMidiMessage(noteBeats, tempo, 1).toTypedArray()
            )
        } catch (ex: Exception) {
            errorHandler.onInvalidNoteBeats()
            return floatArrayOf()
        }

        val midiMessages = (listOf(tempoMessage) + chordMidiMessages + noteMidiMessages).sortedBy { it.time }

        return audioService.generateAudioData(midiMessages, 44_100)
    }
}