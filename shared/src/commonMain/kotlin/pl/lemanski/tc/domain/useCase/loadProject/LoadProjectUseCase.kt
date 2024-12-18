package pl.lemanski.tc.domain.useCase.loadProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.utils.UUID

/**
 * Use case for getting a project by its id
 */
internal interface LoadProjectUseCase {
    operator fun invoke(id: UUID): Project?
}