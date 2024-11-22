package pl.lemanski.tc.domain.useCase.deleteProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.provide

fun deleteProjectUseCase(
    errorHandler: DeleteProjectUseCaseErrorHandler,
    repository: ProjectRepository = provide(),
    block: () -> UUID
): Project? {
    try {
        return repository.deleteProject(block())
    } catch (ex :Exception) {
        errorHandler.handleDeleteProjectError()
    }

    return null
}