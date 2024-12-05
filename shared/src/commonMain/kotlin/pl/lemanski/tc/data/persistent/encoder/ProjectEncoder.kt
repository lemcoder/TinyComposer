package pl.lemanski.tc.data.persistent.encoder

import pl.lemanski.tc.domain.model.project.Project

internal const val projectFileHeader = "id, name, bpm, rhythm, chords, melody"

internal fun Project.encodeToString(): String = with(StringBuilder()) {
    append(projectFileHeader)
    append('\n')
    append("$id, $name, $bpm, ${rhythm.name}, ${chords.encodeToString()}, ${melody.encodeToString()}\n")
}.toString()
