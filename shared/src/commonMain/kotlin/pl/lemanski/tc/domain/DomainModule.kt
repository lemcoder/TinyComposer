package pl.lemanski.tc.domain

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import pl.lemanski.tc.domain.service.navigation.NavigationService

internal object DomainModule {
    fun provide() = module {
        singleOf(::NavigationService)
    }
}