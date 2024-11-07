package pl.lemanski.tc

import android.annotation.SuppressLint
import android.content.Context

// FIXME use jetpack app startup for this
@SuppressLint("StaticFieldLeak")
object ContextProvider {
    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context.applicationContext
    }

    internal fun provide(): Context = context
}