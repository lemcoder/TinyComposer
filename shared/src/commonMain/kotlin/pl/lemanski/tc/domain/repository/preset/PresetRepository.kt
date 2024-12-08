package pl.lemanski.tc.domain.repository.preset

import pl.lemanski.tc.utils.UUID

internal interface PresetRepository {
    fun getChordsPreset(projectId: UUID): Int
    fun getNotesPreset(projectId: UUID): Int

    fun setChordsPreset(projectId: UUID, presetId: Int)
    fun setNotesPreset(projectId: UUID, presetId: Int)
}