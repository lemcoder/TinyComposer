package pl.lemanski.tc.data.chord

import pl.lemanski.tc.domain.model.song.ChordBeats

internal fun List<ChordBeats>.encode(): String =
    map { "${it.first.name}:${it.second}" }.joinToString { ";" }
