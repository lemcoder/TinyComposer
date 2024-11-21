package pl.lemanski.tc.domain.useCase.createProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.provide

fun createProjectUseCase(
    errorHandler: CreateProjectUseCaseErrorHandler,
    projectRepository: ProjectRepository = provide(),
    block: () -> Project
) : Project? {
    val project = block()

    if (project.name.isBlank() || project.name.length > 32) {
        errorHandler.onInvalidProjectName()
        return null
    }

    if (project.bpm !in 30..240) {
        errorHandler.onInvalidProjectBpm()
        return null
    }

    try {
        projectRepository.saveProject(project)
    } catch (ex: Exception) {
        errorHandler.onProjectSaveError()
        return null
    }

    return project
}