package pl.lemanski.tc.domain.service.sharing

internal expect class SharingService() {
    /**
     * Shares file from given path
     */
    fun shareFile(path: String)

    /**
     * Shares text
     */
    fun shareText(text: String)
}