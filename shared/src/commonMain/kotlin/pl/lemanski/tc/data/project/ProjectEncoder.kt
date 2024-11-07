package pl.lemanski.tc.data.project

import pl.lemanski.tc.data.chord.encodeToString
import pl.lemanski.tc.domain.model.project.Project

internal const val projectFileHeader = "id, name, lengthInBeats, bpm, rhythm, chords"

internal fun Project.encodeToString(): String = with(StringBuilder()) {
    append(projectFileHeader)
    append('\n')
    append("$id, $name, $lengthInBeats, $bpm, ${rhythm.name}, ${chords.encodeToString()}\n")
}.toString()
