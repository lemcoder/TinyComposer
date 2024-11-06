package pl.lemanski.tc.domain.model.navigation

internal data class NavigationEvent(
    val destination: Destination,
    val direction: Direction
) {
    enum class Direction {
        FORWARD, BACKWARD
    }
}