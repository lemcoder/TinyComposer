package pl.lemanski.tc.domain.service.sharing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lemanski.tc.utils.Logger
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.MessageUI.MFMailComposeResult
import platform.MessageUI.MFMailComposeViewController
import platform.MessageUI.MFMailComposeViewControllerDelegateProtocol
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIViewController
import platform.darwin.NSObject

internal actual class SharingService {

    actual fun shareFile(path: String) {
        SharingManagerHelper.shareFile(path)
    }

    /**
     * Shares text
     */
    actual fun shareText(text: String) {
        SharingManagerHelper.sharePlainText(text)
    }
}

object SharingManagerHelper {
    private val coroutineContext = CoroutineScope(Job())
    private val log = Logger(this::class)
    private var viewController: UIViewController? = null

    fun setViewController(uiViewController: UIViewController) {
        viewController = uiViewController
    }

    fun shareFile(path: String) = coroutineContext.launch {
        // Need to be called from UI thread
        withContext(Dispatchers.Main) {
            val url = NSURL.fileURLWithPath(path)
            val shareViewController = UIActivityViewController(listOf(url), null)
            viewController?.presentViewController(shareViewController, true) {
                log.info("URL sharing complete")
                this.cancel()
            }
        }
    }

    fun sharePlainText(text: String) = coroutineContext.launch {
        // Need to be called from UI thread
        withContext(Dispatchers.Main) {
            val items = listOf(text)
            val shareViewController = UIActivityViewController(items, null)
            viewController?.presentViewController(shareViewController, true) {
                log.info("Plain text sharing complete")
                this.cancel()
            }
        }
    }
}