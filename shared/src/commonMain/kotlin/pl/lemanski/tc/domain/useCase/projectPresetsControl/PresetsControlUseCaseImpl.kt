package pl.lemanski.tc.domain.useCase.projectPresetsControl

import pl.lemanski.tc.domain.repository.preset.PresetRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal class PresetsControlUseCaseImpl(
    private val presetsRepository: PresetRepository
) : PresetsControlUseCase {
    private val logger = Logger(this::class)

    override fun getChordPreset(projectId: UUID): Int {
        logger.debug("Starting with params: projectId=$projectId")

        val chordPreset = presetsRepository.getChordsPreset(projectId)

        return chordPreset
    }

    override fun getMelodyPreset(projectId: UUID): Int {
        logger.debug("Starting with params: projectId=$projectId")

        val notePreset = presetsRepository.getMelodyPreset(projectId)

        return notePreset
    }

    override fun setPresets(projectId: UUID, chordPreset: Int?, melodyPreset: Int?) {
        logger.debug("Starting with params: id=$projectId, presets=$chordPreset")

        chordPreset?.let {
            presetsRepository.setChordsPreset(projectId, it)
        }

        melodyPreset?.let {
            presetsRepository.setMelodyPreset(projectId, it)
        }
    }
}