package pl.lemanski.tc.domain.useCase.shareFileUseCase

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import pl.lemanski.tc.domain.service.sharing.SharingService
import pl.lemanski.tc.utils.Logger

internal class ShareFileUseCaseImpl(
    private val sharingService: SharingService
): ShareFileUseCase {

    private val logger: Logger = Logger(this::class)

    override fun invoke(errorHandler: ShareFileUseCase.ErrorHandler, path: String) {
        logger.info("Starting with params - path: $path")

        val filePath = Path(path)
        val fileExists = SystemFileSystem.exists(filePath)

        if (!fileExists) {
            logger.error("File does not exist at path: $path")
            errorHandler.onFileNotExistsAtPath()
        }

        sharingService.shareFile(path)
    }
}