package pl.lemanski.tc.data

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.lemanski.tc.data.persistent.TcDatabase
import pl.lemanski.tc.data.persistent.TcDatabaseImpl
import pl.lemanski.tc.data.repository.chord.ChordRepositoryImpl
import pl.lemanski.tc.data.repository.project.ProjectRepositoryImpl
import pl.lemanski.tc.domain.repository.chord.ChordRepository
import pl.lemanski.tc.domain.repository.project.ProjectRepository

object DataModule {
    fun provide() = module {
        singleOf(::TcDatabaseImpl) bind TcDatabase::class
        singleOf(::ProjectRepositoryImpl) bind ProjectRepository::class
        singleOf(::ChordRepositoryImpl) bind ChordRepository::class
    }
}