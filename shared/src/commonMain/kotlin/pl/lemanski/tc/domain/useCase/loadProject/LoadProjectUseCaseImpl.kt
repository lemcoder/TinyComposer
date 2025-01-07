package pl.lemanski.tc.domain.useCase.loadProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal class LoadProjectUseCaseImpl(
    private val projectRepository: ProjectRepository
) : LoadProjectUseCase {

    private val logger = Logger(this::class)

    override fun invoke(id: UUID): Project? {
        logger.debug("Starting with id: $id")
        if (projectRepository.getCurrentProject()?.id == id) {
            logger.debug("Fetching project from cache cached")
            return projectRepository.getCurrentProject()
        }

        val project = projectRepository.getProject(id) ?: run {
            logger.warn("Project not found for id: $id")
            return null
        }

        projectRepository.cacheProject(project)

        logger.debug("Project loaded: $project")
        return project
    }
}