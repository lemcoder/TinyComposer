package pl.lemanski.tc.data.persistent

import android.content.Context
import pl.lemanski.tc.ContextProvider

actual fun getFilesDirPath(): String = ContextProvider.provide().filesDir.absolutePath