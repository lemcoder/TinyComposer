package pl.lemanski.tc

import org.koin.core.context.startKoin
import pl.lemanski.tc.data.DataModule
import pl.lemanski.tc.domain.DomainModule
import pl.lemanski.tc.ui.UiModule

object App {
    val koinInstance = startKoin {
        modules(
            DomainModule.provide(),
            UiModule.provide(),
            DataModule.provide(),
        )
    }
}