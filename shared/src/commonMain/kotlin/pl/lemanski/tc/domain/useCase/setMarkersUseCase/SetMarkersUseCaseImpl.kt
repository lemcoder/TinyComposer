package pl.lemanski.tc.domain.useCase.setMarkersUseCase

import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.utils.Logger

internal class SetMarkersUseCaseImpl : SetMarkersUseCase {
    private val logger: Logger = Logger(SetMarkersUseCaseImpl::class)

    override operator fun invoke(
        project: Project,
        audioStream: AudioStream,
    ) {
        logger.info("Setting markers for project: ${project.id}")

        val beatSize = calculateBeatSize(audioStream, project)
        val numOfChordBeats = project.chords.sumOf { it.second }
        val numOfMelodyBeats = project.melody.sumOf { it.second }
        val maxNumOfBeats = maxOf(numOfMelodyBeats, numOfChordBeats)

        logger.info("Beat size: $beatSize")
        logger.info("Number of chord beats: $numOfChordBeats | Number of melody beats: $numOfMelodyBeats")

        for (i in 0..<maxNumOfBeats) {
            val markerPos = i * beatSize
            logger.info("Adding marker at position: $markerPos")
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