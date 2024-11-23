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
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCase
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCaseImpl

internal object DomainModule {
    fun provide() = module {
        singleOf(::NavigationService)
        singleOf(::AudioMapper)
        singleOf(::AudioService)

        // UseCases

        singleOf(::CreateProjectUseCaseImpl) bind CreateProjectUseCase::class
        singleOf(::DeleteProjectUseCaseImpl) bind DeleteProjectUseCase::class
        singleOf(::GetProjectsListUseCaseImpl) bind GetProjectsListUseCase::class
    }
}