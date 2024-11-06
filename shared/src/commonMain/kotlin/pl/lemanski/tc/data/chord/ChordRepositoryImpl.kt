package pl.lemanski.tc.data.chord

import pl.lemanski.tc.domain.model.song.ChordBeats
import pl.lemanski.tc.domain.repository.chord.ChordRepository
import pl.lemanski.tc.utils.UUID

internal class ChordRepositoryImpl: ChordRepository {
    override fun getChords(projectId: UUID): List<ChordBeats> {
        TODO("Not yet implemented")
    }

    override fun saveChords(projectId: UUID): List<ChordBeats> {
        TODO("Not yet implemented")
    }
}