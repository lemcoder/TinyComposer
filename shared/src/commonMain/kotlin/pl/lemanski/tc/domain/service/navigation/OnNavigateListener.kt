package pl.lemanski.tc.domain.service.navigation

import pl.lemanski.tc.domain.model.navigation.NavigationEvent

fun interface OnNavigateListener {
    fun onNavigate(event: NavigationEvent)
}