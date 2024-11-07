package pl.lemanski.tc.data.project

import pl.lemanski.tc.data.persistent.TcDatabase
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.UUID

internal class ProjectRepositoryImpl : ProjectRepository {
    override fun getProjects(): List<Project> = TcDatabase.getFiles().mapNotNull { getProject(it) }

    override fun getProject(id: UUID): Project? {
        try {
            val project = TcDatabase.loadFile(id).tryParseProject()
            return project
        } catch (ex: Exception) {
            // TODO handle
        }

        return null
    }

    override fun saveProject(project: Project): Project {
        TcDatabase.saveFile(project.id, project.encodeToString())
        return project
    }
}