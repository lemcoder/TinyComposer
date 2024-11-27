package pl.lemanski.tc.domain.useCase.getProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal class GetProjectUseCaseImpl(
    private val projectRepository: ProjectRepository
) : GetProjectUseCase {

    private val logger = Logger(this::class)

    override fun invoke(id: UUID): Project? {
        logger.debug("Starting with id: $id")
        return projectRepository.getProject(id)
    }
}