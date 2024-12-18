package pl.lemanski.tc.domain.useCase.saveProject

import pl.lemanski.tc.domain.model.project.Project

internal interface SaveProjectUseCase {
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