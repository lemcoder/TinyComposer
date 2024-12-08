package pl.lemanski.tc.domain.useCase.updateProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

class UpdateProjectUseCaseImpl(
    private val projectRepository: ProjectRepository,
) : UpdateProjectUseCase {
    private val logger = Logger(this::class)

    override fun invoke(errorHandler: UpdateProjectUseCase.ErrorHandler, project: Project, projectId: UUID): Project? {
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
            projectRepository.deleteProject(projectId)
            projectRepository.saveProject(project)
        } catch (ex: Exception) {
            logger.error("Error saving project: $project", ex)
            errorHandler.onProjectSaveError()
            return null
        }

        return project

    }
}