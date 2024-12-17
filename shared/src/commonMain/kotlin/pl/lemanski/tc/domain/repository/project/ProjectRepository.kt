package pl.lemanski.tc.domain.repository.project

import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.utils.UUID

/**
 * Repository for managing projects.
 */
interface ProjectRepository {
    /**
     * Get all projects from persistent storage
     */
    fun getProjects(): List<Project>

    /**
     * Get project by id from persistent storage
     */
    fun getProject(id: UUID): Project?

    /**
     * Save project to persistent storage
     */
    fun saveProject(project: Project): Project

    /**
     * Delete project from persistent storage
     */
    fun deleteProject(id: UUID): Project?

    /**
     * Cache project in memory
     */
    fun cacheProject(project: Project): Project?

    /**
     * Get current project from memory cache
     */
    fun getCurrentProject(): Project?
}