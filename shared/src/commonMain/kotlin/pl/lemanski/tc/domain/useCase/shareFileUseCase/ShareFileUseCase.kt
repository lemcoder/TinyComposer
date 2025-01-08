package pl.lemanski.tc.domain.useCase.shareFileUseCase

internal interface ShareFileUseCase {

    interface ErrorHandler {
        fun onFileNotExistsAtPath()
    }

    operator fun invoke(errorHandler: ErrorHandler, path: String)
}