package pl.lemanski.tc.domain.useCase.projectPresetsControl

import pl.lemanski.tc.domain.repository.preset.PresetRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal class PresetsControlUseCaseImpl(
    private val presetsRepository: PresetRepository
) : PresetsControlUseCase {
    private val logger = Logger(this::class)

    override fun getPresets(id: UUID): ChordToNotePresets {
        logger.debug("Starting with params: id=$id")

        val chordPreset = presetsRepository.getChordsPreset(id)
        val notePreset = presetsRepository.getMelodyPreset(id)

        return chordPreset to notePreset
    }

    override fun setPresets(id: UUID, presets: ChordToNotePresets) {
        logger.debug("Starting with params: id=$id, presets=$presets")

        presetsRepository.setChordsPreset(id, presets.first)
        presetsRepository.setMelodyPreset(id, presets.second)
    }
}