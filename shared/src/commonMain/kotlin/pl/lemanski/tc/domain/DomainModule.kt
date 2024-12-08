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
import pl.lemanski.tc.domain.useCase.generateAudio.GenerateAudioUseCase
import pl.lemanski.tc.domain.useCase.generateAudio.GenerateAudioUseCaseImpl
import pl.lemanski.tc.domain.useCase.getProject.GetProjectUseCase
import pl.lemanski.tc.domain.useCase.getProject.GetProjectUseCaseImpl
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCase
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCaseImpl
import pl.lemanski.tc.domain.useCase.getSoundFontPresets.GetSoundFontPresetsUseCase
import pl.lemanski.tc.domain.useCase.getSoundFontPresets.GetSoundFontPresetsUseCaseImpl
import pl.lemanski.tc.domain.useCase.playbackControl.PlaybackControlUseCase
import pl.lemanski.tc.domain.useCase.playbackControl.PlaybackControlUseCaseImpl
import pl.lemanski.tc.domain.useCase.projectPresetsControl.PresetsControlUseCase
import pl.lemanski.tc.domain.useCase.projectPresetsControl.PresetsControlUseCaseImpl
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCaseImpl

internal object DomainModule {
    fun provide() = module {
        single<NavigationService> { NavigationService() }
        singleOf(::AudioMapper)
        singleOf(::AudioService)

        // UseCases
        singleOf(::CreateProjectUseCaseImpl) bind CreateProjectUseCase::class
        singleOf(::DeleteProjectUseCaseImpl) bind DeleteProjectUseCase::class
        singleOf(::GetProjectsListUseCaseImpl) bind GetProjectsListUseCase::class
        singleOf(::GenerateAudioUseCaseImpl) bind GenerateAudioUseCase::class
        singleOf(::GetProjectUseCaseImpl) bind GetProjectUseCase::class
        singleOf(::PlaybackControlUseCaseImpl) bind PlaybackControlUseCase::class
        singleOf(::UpdateProjectUseCaseImpl) bind UpdateProjectUseCase::class
        singleOf(::GetSoundFontPresetsUseCaseImpl) bind GetSoundFontPresetsUseCase::class
        singleOf(::PresetsControlUseCaseImpl) bind PresetsControlUseCase::class
    }
}