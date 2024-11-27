package pl.lemanski.tc.domain.useCase.getProjectsList

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger

internal class GetProjectsListUseCaseImpl(
    private val projectRepository: ProjectRepository,
) : GetProjectsListUseCase {

    private val logger = Logger(this::class)

    override operator fun invoke(): List<Project> {
        logger.debug("Starting")

        return projectRepository.getProjects()
    }
}