package pl.lemanski.tc.domain.useCase.getProjectsList

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.provide

fun getProjectsListUseCase(
    projectRepository: ProjectRepository = provide(),
): List<Project> {
    return projectRepository.getProjects()
}