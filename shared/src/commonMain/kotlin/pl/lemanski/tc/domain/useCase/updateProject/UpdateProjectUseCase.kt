package pl.lemanski.tc.domain.useCase.updateProject

import pl.lemanski.tc.domain.model.project.Project

interface UpdateProjectUseCase {
    interface ErrorHandler {
        fun onInvalidProjectName()
        fun onInvalidProjectBpm()
        fun onProjectSaveError()
    }

    operator fun invoke(
        errorHandler: ErrorHandler,
        project: Project
    ): Project?
}