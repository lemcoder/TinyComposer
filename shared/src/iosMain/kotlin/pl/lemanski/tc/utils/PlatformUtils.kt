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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import platform.darwin.DISPATCH_QUEUE_SERIAL
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_attr_t
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t

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

@OptIn(ExperimentalForeignApi::class)
internal fun CoroutineDispatcher.asDispatchQueue(): dispatch_queue_t =
    when (this) {
        Dispatchers.Main -> dispatch_get_main_queue()
        else -> dispatch_queue_create(
            "${toString()}.asDispatchQueue()",
            DISPATCH_QUEUE_SERIAL as dispatch_queue_attr_t
        )
    }