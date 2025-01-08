package pl.lemanski.tc.domain.service.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import org.koin.core.component.KoinComponent
import pl.lemanski.tc.ContextProvider
import java.io.File

internal actual class SharingService : KoinComponent {
    private val context: Context = ContextProvider.context

    actual fun shareFile(path: String) {
        val file = File(path)
        if (!file.exists()) {
            return
        }
        val uri: Uri = FileProvider.getUriForFile(context, "pl.lemanski.tc.FileProvider", file)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = file.extension.asIntentType()
        intent.putExtra(Intent.EXTRA_SUBJECT, file.name)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val chooserIntent = Intent.createChooser(intent, "Share CSV File")

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooserIntent)
        }
    }

    private fun String.asIntentType(): String {
        return when (this) {
            "csv" -> "text/csv"
            "txt" -> "text/plain"
            "pdf" -> "application/pdf"
            else -> "*/*"
        }
    }

    /**
     * Shares text
     */
    actual fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)

        val chooserIntent = Intent.createChooser(intent, "Share Plain Text")

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooserIntent)
        }
    }
}