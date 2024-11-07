package pl.lemanski.tc.domain.service.navigation

import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.NavigationEvent
import pl.lemanski.tc.exception.NavigationStateException

/**
 * Service that is responsible for navigation between [Destination]s
 * This implementation is based on single callback so it is easy to apply to platform navigation
 * on each supported platform
 */
class NavigationService {
    private lateinit var listener: OnNavigateListener
    internal val navStack: ArrayDeque<Destination> = ArrayDeque()

    fun setOnNavigateListener(listener: OnNavigateListener) {
        this.listener = listener
    }

    internal fun goTo(destination: Destination) {
        navStack.addLast(destination)

        listener.onNavigate(NavigationEvent(
            destination = navStack.last(),
            direction = NavigationEvent.Direction.FORWARD
        ))
    }

    internal fun back(): Boolean {
        if (navStack.size <= 1) {
            return false
        }

        navStack.removeLast()

        listener.onNavigate(NavigationEvent(
            destination = navStack.last(),
            direction = NavigationEvent.Direction.BACKWARD
        ))

        return true
    }

    internal inline fun <reified T : Destination> key(): T? {
        val entry: List<T> = navStack.filterIsInstance<T>()
        if (entry.size > 1) throw NavigationStateException("More than one key of the same type on the stack: \n ${navStack.map { "- $it\n" }}")
        return entry.firstOrNull()
    }
}