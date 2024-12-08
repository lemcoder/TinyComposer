package pl.lemanski.tc.domain.useCase.getSoundFontPresets

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository
import pl.lemanski.tc.domain.service.audio.AudioService
import tinycomposer.shared.generated.resources.Res

internal class GetSoundFontPresetsUseCaseImpl(
    private val audioService: AudioService,
    private val soundFontRepository: SoundFontRepository
) : GetSoundFontPresetsUseCase {

    @OptIn(ExperimentalResourceApi::class)
    override fun invoke(): List<Pair<Int, String>> {
        if (!audioService.isSoundFontLoaded()) {
            runBlocking { // FIXME
                Res.readBytes("files/font.sf2").let {
                    audioService.useSoundFont(it)
                }
            }
        }

        return soundFontRepository.getSoundFontPresets()
    }
}