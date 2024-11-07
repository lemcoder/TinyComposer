package pl.lemanski.tc.domain.repository.chord

import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.utils.UUID

interface ChordRepository {
    fun getChords(projectId: UUID): List<ChordBeats>

    fun saveChords(projectId: UUID): List<ChordBeats>

    // TODO add custom chord create ability
}