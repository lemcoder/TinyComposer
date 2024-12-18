package pl.lemanski.tc.domain.useCase.projectPresetsControl

import pl.lemanski.tc.domain.repository.preset.PresetRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal class PresetsControlUseCaseImpl(
    private val presetsRepository: PresetRepository
) : PresetsControlUseCase {
    private val logger = Logger(this::class)

    override fun getPresets(projectId: UUID): ChordToNotePresets {
        logger.debug("Starting with params: projectId=$projectId")

        val chordPreset = presetsRepository.getChordsPreset(projectId)
        val notePreset = presetsRepository.getMelodyPreset(projectId)

        return chordPreset to notePreset
    }

    override fun setPresets(projectId: UUID, presets: ChordToNotePresets) {
        logger.debug("Starting with params: id=$projectId, presets=$presets")

        presetsRepository.setChordsPreset(projectId, presets.first)
        presetsRepository.setMelodyPreset(projectId, presets.second)
    }
}