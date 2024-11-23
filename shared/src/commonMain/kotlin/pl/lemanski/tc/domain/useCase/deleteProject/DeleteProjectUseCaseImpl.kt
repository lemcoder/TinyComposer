package pl.lemanski.tc.domain.useCase.deleteProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import kotlin.math.log

internal class DeleteProjectUseCaseImpl(
    private val repository: ProjectRepository
) : DeleteProjectUseCase {
    private val logger = Logger(this::class)

    override fun invoke(errorHandler: DeleteProjectUseCase.ErrorHandler, projectId: UUID): Project? {
        try {
            logger.debug("Deleting project with id: $projectId")
            return repository.deleteProject(projectId)
        } catch (ex: Exception) {
            logger.error("Deleting project failed: $ex")
            errorHandler.handleDeleteProjectError()
        }

        return null
    }
}