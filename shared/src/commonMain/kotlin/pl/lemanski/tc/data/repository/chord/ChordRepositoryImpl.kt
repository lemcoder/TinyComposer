package pl.lemanski.tc.data.repository.chord

import pl.lemanski.tc.data.persistent.TcDatabase
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.repository.chord.ChordRepository
import pl.lemanski.tc.utils.UUID

internal class ChordRepositoryImpl(
    private val database: TcDatabase
): ChordRepository {
    override fun getChords(projectId: UUID): List<ChordBeats> {
        TODO("Not yet implemented")
    }

    override fun saveChords(projectId: UUID): List<ChordBeats> {
        TODO("Not yet implemented")
    }
}