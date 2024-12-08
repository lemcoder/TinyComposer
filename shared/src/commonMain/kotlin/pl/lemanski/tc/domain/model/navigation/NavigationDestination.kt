package pl.lemanski.tc.domain.model.navigation

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import pl.lemanski.tc.utils.UUID

sealed interface Destination : ViewModelStoreOwner

data object WelcomeDestination : Destination {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}

data object ProjectListDestination : Destination {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}

data class ProjectDetailsDestination(val projectId: UUID) : Destination {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}

data class ProjectAiGenerateDestination(val projectId: UUID) : Destination {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}

data object ProjectCreateDestination : Destination {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}

data class ProjectOptionsDestination(val projectId: UUID) : Destination {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}