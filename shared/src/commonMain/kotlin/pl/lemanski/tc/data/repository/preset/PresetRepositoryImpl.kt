package pl.lemanski.tc.data.repository.preset

import pl.lemanski.tc.data.source.cache.MemoryCache
import pl.lemanski.tc.domain.repository.preset.PresetRepository
import pl.lemanski.tc.utils.UUID

internal class PresetRepositoryImpl(
    private val memoryCache: MemoryCache
) : PresetRepository {
    override fun getChordsPreset(projectId: UUID): Int {
        return memoryCache.get(projectId.toString() + "chord_preset") ?: 0
    }

    override fun getMelodyPreset(projectId: UUID): Int {
        return memoryCache.get(projectId.toString() + "melody_preset") ?: 0
    }

    override fun setChordsPreset(projectId: UUID, presetId: Int) {
        memoryCache.put(projectId.toString() + "chord_preset", presetId)
    }

    override fun setMelodyPreset(projectId: UUID, presetId: Int) {
        memoryCache.put(projectId.toString() + "melody_preset", presetId)
    }
}