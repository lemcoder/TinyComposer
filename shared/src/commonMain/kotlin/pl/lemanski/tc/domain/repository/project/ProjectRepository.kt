package pl.lemanski.tc.domain.repository.project

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.utils.UUID

interface ProjectRepository {
    fun getProjects(): List<Project>

    fun getProject(id: UUID): Project?

    fun saveProject(project: Project): Project

    fun deleteProject(id: UUID): Project?
}