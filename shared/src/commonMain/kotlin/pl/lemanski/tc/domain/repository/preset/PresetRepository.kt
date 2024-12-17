package pl.lemanski.tc.domain.repository.preset

import pl.lemanski.tc.utils.UUID

internal interface PresetRepository {
    fun getChordsPreset(projectId: UUID): Int
    fun getMelodyPreset(projectId: UUID): Int

    fun setChordsPreset(projectId: UUID, presetId: Int)
    fun setMelodyPreset(projectId: UUID, presetId: Int)
}