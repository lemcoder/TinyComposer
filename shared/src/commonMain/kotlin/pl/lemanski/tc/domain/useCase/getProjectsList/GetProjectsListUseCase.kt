package pl.lemanski.tc.domain.useCase.getProjectsList

import pl.lemanski.tc.domain.model.project.Project

internal interface GetProjectsListUseCase {
    operator fun invoke(): List<Project>
}