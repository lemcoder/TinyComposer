package pl.lemanski.tc.data

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.lemanski.tc.data.cache.MemoryCache
import pl.lemanski.tc.data.persistent.TcDatabase
import pl.lemanski.tc.data.persistent.TcDatabaseImpl
import pl.lemanski.tc.data.remote.genAi.client.GeminiClient
import pl.lemanski.tc.data.remote.genAi.client.GenAiClient
import pl.lemanski.tc.data.repository.chord.ChordRepositoryImpl
import pl.lemanski.tc.data.repository.genAi.GenAiRepositoryImpl
import pl.lemanski.tc.data.repository.preset.PresetRepositoryImpl
import pl.lemanski.tc.data.repository.project.ProjectRepositoryImpl
import pl.lemanski.tc.data.repository.soundFont.SoundFontRepositoryImpl
import pl.lemanski.tc.domain.repository.chord.ChordRepository
import pl.lemanski.tc.domain.repository.genAi.GenAiRepository
import pl.lemanski.tc.domain.repository.preset.PresetRepository
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository

object DataModule {
    fun provide() = module {
        singleOf(::MemoryCache)
        singleOf(::TcDatabaseImpl) bind TcDatabase::class
        singleOf(::ProjectRepositoryImpl) bind ProjectRepository::class
        singleOf(::ChordRepositoryImpl) bind ChordRepository::class
        singleOf(::SoundFontRepositoryImpl) bind SoundFontRepository::class
        singleOf(::GeminiClient) bind GenAiClient::class
        singleOf(::GenAiRepositoryImpl) bind GenAiRepository::class
        singleOf(::PresetRepositoryImpl) bind PresetRepository::class
    }
}