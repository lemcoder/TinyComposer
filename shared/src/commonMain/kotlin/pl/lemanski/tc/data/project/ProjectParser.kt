package pl.lemanski.tc.data.project

import pl.lemanski.tc.data.chord.tryDecodeChordBeats
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
    val length = data[2].trim().toInt()
    val bpm = data[3].trim().toInt()
    val rhythm = Rhythm.entries.find { it.name == data[4].trim() }!!
    val chords = data[5].trim().tryDecodeChordBeats()

    return Project(
        id = id,
        name = name,
        lengthInBeats = length,
        bpm = bpm,
        rhythm = rhythm,
        chords = chords
    )
}