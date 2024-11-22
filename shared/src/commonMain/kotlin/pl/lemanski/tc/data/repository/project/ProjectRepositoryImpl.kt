package pl.lemanski.tc.data.repository.project

import pl.lemanski.tc.data.persistent.TcDatabase
import pl.lemanski.tc.data.persistent.decoder.tryParseProject
import pl.lemanski.tc.data.persistent.encoder.encodeToString
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal class ProjectRepositoryImpl(
    private val database: TcDatabase
) : ProjectRepository {
    private val logger = Logger(this::class)

    override fun getProjects(): List<Project> = database.getFiles().mapNotNull { getProject(it) }

    override fun getProject(id: UUID): Project? {
        try {
            val project = database.loadFile(id).tryParseProject()
            return project
        } catch (ex: Exception) {
            logger.error("Failed to load project: $id", ex)
        }

        return null
    }

    override fun saveProject(project: Project): Project {
        database.saveFile(project.id, project.encodeToString())
        return project
    }

    override fun deleteProject(id: UUID): Project? {
        try {
            val project = getProject(id)

            if (project == null) {
                logger.error("Project not found: $id")
                return null
            }

            database.deleteFile(id)

            return project
        } catch (ex: Exception) {
            logger.error("Failed to delete project: $id", ex)
        }

        return null
    }
}