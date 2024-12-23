package pl.lemanski.tc.domain.useCase.setMarkersUseCase

import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.utils.Logger

internal class SetMarkersUseCaseImpl : SetMarkersUseCase {
    private val logger: Logger = Logger(SetMarkersUseCaseImpl::class)

    override operator fun invoke(
        project: Project,
        audioStream: AudioStream,
        option: SetMarkersUseCase.Option
    ) {
        logger.info("Setting markers for project: ${project.id} ${option.name}")

        // TODO add error handling
        when (option) {
            SetMarkersUseCase.Option.CHORDS -> setMarkersForChordBeats(project, audioStream)
            SetMarkersUseCase.Option.MELODY -> setMarkersForNoteBeats(project, audioStream)
        }
    }

    private fun setMarkersForChordBeats(
        project: Project,
        audioStream: AudioStream
    ) {
        val beatSize = calculateBeatSize(audioStream, project)

        var currentBeatPos = 0
        for (i in project.chords.indices) {
            currentBeatPos += project.chords.getOrNull(i - 1)?.second ?: 0
            val markerPos = currentBeatPos * beatSize
            logger.info("Adding marker at position: $markerPos")
            audioStream.addMarker(markerPos)
        }
    }

    private fun setMarkersForNoteBeats(
        project: Project,
        audioStream: AudioStream
    ) {
        val beatSize = calculateBeatSize(audioStream, project)

        var currentBeatPos = 0
        for (i in project.melody.indices) {
            currentBeatPos += project.melody.getOrNull(i - 1)?.second ?: 0
            val markerPos = currentBeatPos * beatSize
            logger.warn("Adding marker at position: $markerPos")
            audioStream.addMarker(markerPos)
        }
    }

    private fun calculateBeatSize(audioStream: AudioStream, project: Project): Int {
        val chordsSum = project.chords.sumOf { it.second }
        val melodySum = project.melody.sumOf { it.second }

        val beatSize = audioStream.data.size / maxOf(chordsSum, melodySum).toDouble()
        return beatSize.toInt()
    }
}