package pl.lemanski.tc.domain.useCase.getProject

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.utils.UUID

internal interface GetProjectUseCase {
    operator fun invoke(id: UUID): Project?
}