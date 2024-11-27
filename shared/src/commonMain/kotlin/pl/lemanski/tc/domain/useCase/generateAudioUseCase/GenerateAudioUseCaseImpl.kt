package pl.lemanski.tc.domain.useCase.generateAudioUseCase

import org.jetbrains.compose.resources.ExperimentalResourceApi
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.service.audio.AudioService
import tinycomposer.shared.generated.resources.Res

internal class GenerateAudioUseCaseImpl(
    private val audioService: AudioService,
) : GenerateAudioUseCase {

    @OptIn(ExperimentalResourceApi::class)
    override suspend operator fun invoke(
        errorHandler: GenerateAudioUseCase.ErrorHandler,
        chordBeats: List<ChordBeats>,
        tempo: Int
    ): FloatArray {
        if (!audioService.isSoundFontLoaded()) {
            Res.readBytes("files/font.sf2").let {
                audioService.useSoundFont(it)
            }
        }

        return audioService.generateAudioData(chordBeats, 44_100, tempo)
    }
}