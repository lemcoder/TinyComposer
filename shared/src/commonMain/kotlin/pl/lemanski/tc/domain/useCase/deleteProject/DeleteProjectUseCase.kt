package pl.lemanski.tc.domain.useCase.deleteProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.utils.UUID

internal interface DeleteProjectUseCase {
    interface ErrorHandler {
        fun handleDeleteProjectError()
    }

    operator fun invoke(errorHandler: ErrorHandler, projectId: UUID): Project?
}