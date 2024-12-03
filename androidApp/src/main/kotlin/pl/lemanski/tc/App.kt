package pl.lemanski.tc

import android.app.Application
import org.koin.android.ext.koin.androidContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TCApp.start()
        TCApp.koinInstance.androidContext(this)
    }
}