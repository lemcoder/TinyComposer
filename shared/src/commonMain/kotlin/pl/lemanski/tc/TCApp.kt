package pl.lemanski.tc

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import pl.lemanski.tc.data.DataModule
import pl.lemanski.tc.domain.DomainModule
import pl.lemanski.tc.ui.UiModule

object TCApp {
    lateinit var koinInstance: KoinApplication

    fun start() {
        koinInstance = startKoin {
            modules(
                DomainModule.provide(),
                UiModule.provide(),
                DataModule.provide(),
            )
        }
    }
}