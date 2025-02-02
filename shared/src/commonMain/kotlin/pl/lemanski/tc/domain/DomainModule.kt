package pl.lemanski.tc.domain

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.lemanski.tc.domain.service.audio.AudioMapper
import pl.lemanski.tc.domain.service.audio.AudioService
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.sharing.SharingService
import pl.lemanski.tc.domain.useCase.aiGenerate.AiGenerateUseCase
import pl.lemanski.tc.domain.useCase.aiGenerate.AiGenerateUseCaseImpl
import pl.lemanski.tc.domain.useCase.deleteProject.DeleteProjectUseCase
import pl.lemanski.tc.domain.useCase.deleteProject.DeleteProjectUseCaseImpl
import pl.lemanski.tc.domain.useCase.generateAudio.GenerateAudioUseCase
import pl.lemanski.tc.domain.useCase.generateAudio.GenerateAudioUseCaseImpl
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCase
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCaseImpl
import pl.lemanski.tc.domain.useCase.getSoundFontPresets.GetSoundFontPresetsUseCase
import pl.lemanski.tc.domain.useCase.getSoundFontPresets.GetSoundFontPresetsUseCaseImpl
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCase
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCaseImpl
import pl.lemanski.tc.domain.useCase.projectPresetsControl.PresetsControlUseCase
import pl.lemanski.tc.domain.useCase.projectPresetsControl.PresetsControlUseCaseImpl
import pl.lemanski.tc.domain.useCase.saveProject.SaveProjectUseCase
import pl.lemanski.tc.domain.useCase.saveProject.SaveProjectUseCaseImpl
import pl.lemanski.tc.domain.useCase.setMarkersUseCase.SetMarkersUseCase
import pl.lemanski.tc.domain.useCase.setMarkersUseCase.SetMarkersUseCaseImpl
import pl.lemanski.tc.domain.useCase.shareFileUseCase.ShareFileUseCase
import pl.lemanski.tc.domain.useCase.shareFileUseCase.ShareFileUseCaseImpl
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCaseImpl

internal object DomainModule {
    fun provide() = module {
        single<NavigationService> { NavigationService() }
        singleOf(::AudioMapper)
        singleOf(::AudioService)
        singleOf(::SharingService)

        // UseCases
        singleOf(::SaveProjectUseCaseImpl) bind SaveProjectUseCase::class
        singleOf(::DeleteProjectUseCaseImpl) bind DeleteProjectUseCase::class
        singleOf(::GetProjectsListUseCaseImpl) bind GetProjectsListUseCase::class
        singleOf(::GenerateAudioUseCaseImpl) bind GenerateAudioUseCase::class
        singleOf(::LoadProjectUseCaseImpl) bind LoadProjectUseCase::class
        singleOf(::UpdateProjectUseCaseImpl) bind UpdateProjectUseCase::class
        singleOf(::SetMarkersUseCaseImpl) bind SetMarkersUseCase::class
        singleOf(::GetSoundFontPresetsUseCaseImpl) bind GetSoundFontPresetsUseCase::class
        singleOf(::PresetsControlUseCaseImpl) bind PresetsControlUseCase::class
        singleOf(::AiGenerateUseCaseImpl) bind AiGenerateUseCase::class
        singleOf(::ShareFileUseCaseImpl) bind ShareFileUseCase::class
    }
}