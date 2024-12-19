package pl.lemanski.tc.domain.useCase.getSoundFontPresets

import pl.lemanski.tc.domain.repository.soundFont.SoundFontRepository

internal class GetSoundFontPresetsUseCaseImpl(
    private val soundFontRepository: SoundFontRepository
) : GetSoundFontPresetsUseCase {

    override fun invoke(): Map<Int, String> {
        return soundFontRepository.getSoundFontPresets()
    }
}