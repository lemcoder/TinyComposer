package pl.lemanski.tc.data.project

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.UUID

internal class ProjectRepositoryImpl: ProjectRepository {
    override fun getProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun getProject(id: UUID): Project? {
        TODO("Not yet implemented")
    }

    override fun saveProject(project: Project): Project {
        TODO("Not yet implemented")
    }
}