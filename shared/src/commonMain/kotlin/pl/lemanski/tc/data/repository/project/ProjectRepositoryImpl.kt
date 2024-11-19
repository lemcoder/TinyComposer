package pl.lemanski.tc.data.repository.project

import pl.lemanski.tc.data.persistent.TcDatabase
import pl.lemanski.tc.data.persistent.decoder.tryParseProject
import pl.lemanski.tc.data.persistent.encoder.encodeToString
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.UUID

internal class ProjectRepositoryImpl(
    private val database: TcDatabase
) : ProjectRepository {
    override fun getProjects(): List<Project> = database.getFiles().mapNotNull { getProject(it) }

    override fun getProject(id: UUID): Project? {
        try {
            val project = database.loadFile(id).tryParseProject()
            return project
        } catch (ex: Exception) {
            // TODO handle
        }

        return null
    }

    override fun saveProject(project: Project): Project {
        database.saveFile(project.id, project.encodeToString())
        return project
    }
}