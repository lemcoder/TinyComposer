package pl.lemanski.tc.domain.useCase.getSoundFontPresets

interface GetSoundFontPresetsUseCase {
    operator fun invoke(): List<Pair<Int, String>>
}