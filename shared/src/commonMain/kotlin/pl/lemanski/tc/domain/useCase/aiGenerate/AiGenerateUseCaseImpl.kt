package pl.lemanski.tc.domain.useCase.aiGenerate

import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats
import pl.lemanski.tc.domain.repository.genAi.GenAiRepository
import pl.lemanski.tc.utils.Logger

internal class AiGenerateUseCaseImpl(
    private val genAiRepository: GenAiRepository
) : AiGenerateUseCase {
    private val logger: Logger = Logger(this::class)

    override suspend fun generateChordBeats(errorHandler: AiGenerateUseCase.ErrorHandler, prompt: String): List<ChordBeats> {
        return try {
            genAiRepository.generateChordBeats(prompt)
        } catch (ex: Exception) {
            logger.error("Failed to generate chord beats", ex)
            errorHandler.onUnknownError() // TODO handle error
            listOf()
        }
    }

    override suspend fun generateMelody(errorHandler: AiGenerateUseCase.ErrorHandler, prompt: String): List<NoteBeats> {
        return try {
            genAiRepository.generateMelody(prompt)
        } catch (ex: Exception) {
            logger.error("Failed to generate chord beats", ex)
            errorHandler.onUnknownError() // TODO handle error
            listOf()
        }
    }
}