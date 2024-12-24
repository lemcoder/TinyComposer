package pl.lemanski.tc.domain.useCase.setMarkersUseCase

import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.project.Project

internal interface SetMarkersUseCase {
    operator fun invoke(
        project: Project,
        audioStream: AudioStream,
    )
}