package pl.lemanski.tc.domain.useCase.createProject

import pl.lemanski.tc.domain.model.project.Project

internal interface CreateProjectUseCase {
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