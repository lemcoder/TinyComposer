package pl.lemanski.tc

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ContextProvider : KoinComponent {
    val context: Context by inject()
}