package pl.lemanski.tc.domain.model.navigation

sealed interface Destination

data object WelcomeDestination : Destination

data object ProjectsDestination: Destination

data class ProjectDetailsDestination(val projectId: String) : Destination

data class AiGenerateDestination(val projectId: String) : Destination

data object ProjectCreateDestination : Destination