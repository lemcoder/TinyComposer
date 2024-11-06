package pl.lemanski.tc.domain.service.navigation

import kotlinx.coroutines.flow.StateFlow
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.NavigationEvent
import pl.lemanski.tc.exception.NavigationStateException

internal abstract class NavigationService {
    abstract val navStack: ArrayDeque<Destination>
    abstract val navigationState: StateFlow<NavigationEvent>

    internal abstract fun navigate(reducer: (NavigationEvent) -> NavigationEvent)
}

internal fun NavigationService.goTo(destination: Destination) {
    navStack.addLast(destination)

    navigate {
        NavigationEvent(
            destination = navStack.last(),
            direction = NavigationEvent.Direction.FORWARD
        )
    }
}

internal fun NavigationService.back(): Unit? {
    if (navStack.size <= 1) {
        return null
    }

    navStack.removeLast()

    return navigate {
        NavigationEvent(
            destination = navStack.last(),
            direction = NavigationEvent.Direction.BACKWARD
        )
    }
}

internal inline fun <reified T : Destination> NavigationService.key(): T? {
    val entry: List<T> = navStack.filterIsInstance<T>()
    if (entry.size > 1) throw NavigationStateException("More than one key of the same type on the stack: \n ${navStack.map { "- $it\n" }}")
    return entry.firstOrNull()
}