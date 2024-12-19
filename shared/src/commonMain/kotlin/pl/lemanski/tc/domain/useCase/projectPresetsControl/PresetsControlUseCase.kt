package pl.lemanski.tc.domain.useCase.projectPresetsControl

import pl.lemanski.tc.utils.UUID

typealias ChordToNotePresets = Pair<Int, Int>

internal interface PresetsControlUseCase {
    fun getChordPreset(projectId: UUID): Int

    fun getMelodyPreset(projectId: UUID): Int

    fun setPresets(projectId: UUID, chordPreset: Int? = null, melodyPreset: Int? = null)
}