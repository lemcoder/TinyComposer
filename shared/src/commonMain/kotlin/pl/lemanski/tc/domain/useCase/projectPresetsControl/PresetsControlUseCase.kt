package pl.lemanski.tc.domain.useCase.projectPresetsControl

import pl.lemanski.tc.utils.UUID

typealias ChordToNotePresets = Pair<Int, Int>

internal interface PresetsControlUseCase {
    fun getPresets(id: UUID): ChordToNotePresets
    fun setPresets(id: UUID, presets: ChordToNotePresets)
}