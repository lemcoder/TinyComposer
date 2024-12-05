package pl.lemanski.tc.domain.useCase.aiGenerateUseCase

import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats

internal interface AiGenerateUseCase {
    interface ErrorHandler {
        fun onParsingError()
        fun onNetworkError()
        fun onUnknownError()
    }

    suspend fun generateChordBeats(errorHandler: ErrorHandler, prompt: String): List<ChordBeats>

    suspend fun generateMelody(errorHandler: ErrorHandler, prompt: String): List<NoteBeats>
}