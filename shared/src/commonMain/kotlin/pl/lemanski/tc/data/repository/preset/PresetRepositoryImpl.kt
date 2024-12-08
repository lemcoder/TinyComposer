package pl.lemanski.tc.data.repository.preset

import pl.lemanski.tc.data.cache.MemoryCache
import pl.lemanski.tc.domain.repository.preset.PresetRepository
import pl.lemanski.tc.utils.UUID

internal class PresetRepositoryImpl(
    val memoryCache: MemoryCache
) : PresetRepository {
    override fun getChordsPreset(projectId: UUID): Int {
        return memoryCache.get<Int>(projectId.toString() + "chord") ?: 0
    }

    override fun getNotesPreset(projectId: UUID): Int {
        return memoryCache.get<Int>(projectId.toString() + "note") ?: 0
    }

    override fun setChordsPreset(projectId: UUID, presetId: Int) {
        memoryCache.put(projectId.toString() + "chord", presetId)
    }

    override fun setNotesPreset(projectId: UUID, presetId: Int) {
        memoryCache.put(projectId.toString() + "note", presetId)
    }
}