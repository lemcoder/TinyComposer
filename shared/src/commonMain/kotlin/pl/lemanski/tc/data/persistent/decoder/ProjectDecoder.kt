package pl.lemanski.tc.data.persistent.decoder

import pl.lemanski.tc.data.persistent.encoder.projectFileHeader
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.utils.UUID

internal fun String.tryParseProject(): Project {
    val header = lines().first()

    if (!header.contentEquals(projectFileHeader)) {
        throw IllegalArgumentException("Invalid project file header: \n$header")
    }

    val data = lines()[1].trim().split(",")

    val id = UUID(data[0].trim())
    val name = data[1].trim()
    val bpm = data[2].trim().toInt()
    val rhythm = Rhythm.entries.find { it.name == data[3].trim() }!!
    val chords = data[4].trim().tryDecodeChordBeats()
    val melody = data[5].trim().tryDecodeNoteBeats()

    return Project(
        id = id,
        name = name,
        bpm = bpm,
        rhythm = rhythm,
        chords = chords,
        melody = melody
    )
}