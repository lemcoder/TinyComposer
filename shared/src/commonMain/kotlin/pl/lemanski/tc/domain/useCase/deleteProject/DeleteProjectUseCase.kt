package pl.lemanski.tc.domain.useCase.deleteProject

import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.provide

fun deleteProjectUseCase(
    errorHandler: DeleteProjectUseCaseErrorHandler,
    repository: ProjectRepository = provide(),
    block: () -> UUID
) {
    try {
        repository.deleteProject(block())
    } catch (ex :Exception) {
        errorHandler.handleDeleteProjectError()
    }
}