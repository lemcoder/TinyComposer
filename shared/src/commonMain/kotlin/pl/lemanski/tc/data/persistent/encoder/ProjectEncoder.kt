package pl.lemanski.tc.data.persistent.encoder

import pl.lemanski.tc.domain.model.project.Project

internal const val projectFileHeader = "id, name, lengthInBeats, bpm, rhythm, chords"

internal fun Project.encodeToString(): String = with(StringBuilder()) {
    append(projectFileHeader)
    append('\n')
    append("$id, $name, $lengthInMeasures, $bpm, ${rhythm.name}, ${chords.encodeToString()}\n")
}.toString()
