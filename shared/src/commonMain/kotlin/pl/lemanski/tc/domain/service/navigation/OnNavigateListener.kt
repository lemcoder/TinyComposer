package pl.lemanski.tc.domain.service.navigation

import pl.lemanski.tc.domain.model.navigation.NavigationEvent

interface OnNavigateListener {
    fun onNavigate(event: NavigationEvent)
}