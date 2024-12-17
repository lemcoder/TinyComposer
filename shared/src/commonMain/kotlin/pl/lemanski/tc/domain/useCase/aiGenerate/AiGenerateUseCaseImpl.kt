package pl.lemanski.tc.domain.useCase.aiGenerate

import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.repository.nlp.NlpRepository
import pl.lemanski.tc.utils.Logger

internal class AiGenerateUseCaseImpl(
    private val nlpRepository: NlpRepository
) : AiGenerateUseCase {
    private val logger: Logger = Logger(this::class)

    override suspend fun generateChordBeats(errorHandler: AiGenerateUseCase.ErrorHandler, prompt: String): List<ChordBeats> {
        return try {
            nlpRepository.generateChordBeats(prompt)
        } catch (ex: Exception) {
            logger.error("Failed to generate chord beats", ex)
            errorHandler.onUnknownError() // TODO handle error
            listOf()
        }
    }

    override suspend fun generateMelody(errorHandler: AiGenerateUseCase.ErrorHandler, prompt: String): List<NoteBeats> {
        return try {
            nlpRepository.generateMelody(prompt)
        } catch (ex: Exception) {
            logger.error("Failed to generate chord beats", ex)
            errorHandler.onUnknownError() // TODO handle error
            listOf()
        }
    }
}