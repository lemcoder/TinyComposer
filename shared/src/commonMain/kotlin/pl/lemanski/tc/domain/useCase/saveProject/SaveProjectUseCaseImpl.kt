package pl.lemanski.tc.domain.useCase.saveProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger

internal class SaveProjectUseCaseImpl(
    private val projectRepository: ProjectRepository,
) : SaveProjectUseCase {
    private val logger = Logger(this::class)

    override operator fun invoke(
        errorHandler: SaveProjectUseCase.ErrorHandler,
        project: Project
    ): Project? {
        logger.debug("Starting with: $project")

        if (project.name.isBlank() || project.name.length > 32) {
            logger.warn("Invalid project name: ${project.name}")
            errorHandler.onInvalidProjectName()
            return null
        }

        if (project.bpm !in 30..240) {
            logger.warn("Invalid project bpm: ${project.bpm}")
            errorHandler.onInvalidProjectBpm()
            return null
        }

        try {
            projectRepository.saveProject(project)
        } catch (ex: Exception) {
            logger.error("Error saving project: $project", ex)
            errorHandler.onProjectSaveError()
            return null
        }

        return project
    }
}