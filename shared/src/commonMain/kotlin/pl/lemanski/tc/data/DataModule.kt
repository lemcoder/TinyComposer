package pl.lemanski.tc.data

import io.github.lemcoder.mikrosoundfont.MikroSoundFont
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.lemanski.tc.data.repository.nlp.NlpRepositoryImpl
import pl.lemanski.tc.data.repository.preset.PresetRepositoryImpl
import pl.lemanski.tc.data.repository.project.ProjectRepositoryImpl
import pl.lemanski.tc.data.repository.soundFont.SoundFontRepositoryImpl
import pl.lemanski.tc.data.source.cache.MemoryCache
import pl.lemanski.tc.data.source.persistent.FileSystemDatabase
import pl.lemanski.tc.data.source.persistent.FileSystemDatabaseImpl
import pl.lemanski.tc.data.source.persistent.ResourcesLoader
import pl.lemanski.tc.data.source.persistent.ResourcesLoaderImpl
import pl.lemanski.tc.data.source.remote.GeminiClient
import pl.lemanski.tc.data.source.remote.GenAiClient
import pl.lemanski.tc.domain.repository.nlp.NlpRepository
import pl.lemanski.tc.domain.repository.preset.PresetRepository
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository

object DataModule {
    fun provide() = module {
        singleOf(::MemoryCache)
        singleOf(::FileSystemDatabaseImpl) bind FileSystemDatabase::class
        singleOf(::ProjectRepositoryImpl) bind ProjectRepository::class
        singleOf(::GeminiClient) bind GenAiClient::class
        singleOf(::PresetRepositoryImpl) bind PresetRepository::class
        singleOf(::ResourcesLoaderImpl) bind ResourcesLoader::class

        single<SoundFontRepository> { SoundFontRepositoryImpl(get(), Dispatchers.IO, MikroSoundFont, get()) }
        single<NlpRepository> { NlpRepositoryImpl(get(), Dispatchers.IO) }
    }
}