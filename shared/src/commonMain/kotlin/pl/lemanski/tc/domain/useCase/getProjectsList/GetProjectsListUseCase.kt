package pl.lemanski.tc.domain.useCase.getProjectsList

import pl.lemanski.tc.domain.model.project.Project

interface GetProjectsListUseCase {
    operator fun invoke(): List<Project>
}