package pl.lemanski.tc.domain.model.navigation

sealed interface Destination

data object StartDestination : Destination

data object ProjectsDestination: Destination