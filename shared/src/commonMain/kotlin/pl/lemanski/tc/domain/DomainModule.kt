package pl.lemanski.tc.domain

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.lemanski.tc.domain.service.audio.AudioMapper
import pl.lemanski.tc.domain.service.audio.AudioService
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.useCase.createProject.CreateProjectUseCase
import pl.lemanski.tc.domain.useCase.createProject.CreateProjectUseCaseImpl
import pl.lemanski.tc.domain.useCase.deleteProject.DeleteProjectUseCase
import pl.lemanski.tc.domain.useCase.deleteProject.DeleteProjectUseCaseImpl
import pl.lemanski.tc.domain.useCase.generateAudioUseCase.GenerateAudioUseCase
import pl.lemanski.tc.domain.useCase.generateAudioUseCase.GenerateAudioUseCaseImpl
import pl.lemanski.tc.domain.useCase.getProject.GetProjectUseCase
import pl.lemanski.tc.domain.useCase.getProject.GetProjectUseCaseImpl
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCase
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCaseImpl
import pl.lemanski.tc.domain.useCase.playbackControlUseCase.PlaybackControlUseCase
import pl.lemanski.tc.domain.useCase.playbackControlUseCase.PlaybackControlUseCaseImpl

internal object DomainModule {
    fun provide() = module {
        single <NavigationService> { NavigationService() }
        singleOf(::AudioMapper)
        singleOf(::AudioService)

        // UseCases

        singleOf(::CreateProjectUseCaseImpl) bind CreateProjectUseCase::class
        singleOf(::DeleteProjectUseCaseImpl) bind DeleteProjectUseCase::class
        singleOf(::GetProjectsListUseCaseImpl) bind GetProjectsListUseCase::class
        singleOf(::GenerateAudioUseCaseImpl) bind GenerateAudioUseCase::class
        singleOf(::GetProjectUseCaseImpl) bind GetProjectUseCase::class
        singleOf(::PlaybackControlUseCaseImpl) bind PlaybackControlUseCase::class
    }
}