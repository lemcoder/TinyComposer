package pl.lemanski.tc.utils

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create

@OptIn(ExperimentalForeignApi::class)
internal fun getDirUrl(directory: NSSearchPathDirectory, create: Boolean = false): NSURL? {
    memScoped {
        val error = alloc<ObjCObjectVar<NSError?>>()
        return NSFileManager.defaultManager.URLForDirectory(directory, NSUserDomainMask, null, create, error.ptr)?.standardizedURL
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): NSData = this.usePinned {
    NSData.create(bytes = it.addressOf(0), length = this.size.convert())
}

internal interface NativeRethrowScope {
    fun onError(handler: (Exception) -> Unit)
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun nativeRunCatching(block: NativeRethrowScope.(CPointer<ObjCObjectVar<NSError?>>) -> Unit) {
    val scope = object : NativeRethrowScope {
        var errorHandler: ((Exception) -> Unit)? = null

        override fun onError(handler: (Exception) -> Unit) {
            errorHandler = handler
        }
    }

    memScoped {
        val errorPtr = alloc<ObjCObjectVar<NSError?>>()
        scope.block(errorPtr.ptr)

        if (errorPtr.value != null) {
            scope.errorHandler?.invoke(Exception(errorPtr.value!!.description))
        }
    }
}