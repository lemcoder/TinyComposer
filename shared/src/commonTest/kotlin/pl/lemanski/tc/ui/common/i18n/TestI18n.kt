package pl.lemanski.tc.ui.common.i18n

internal class TestI18n : I18n {
    override val common = object : I18n.Common {
        override val ok: String = "ok"
        override val cancel: String = "cancel"
        override val close: String = "close"
        override val yes: String = "yes"
        override val no: String = "no"
        override val confirm: String = "confirm"
        override val delete: String = "delete"
        override val edit: String = "edit"
        override val add: String = "add"
        override val save: String = "save"
        override val reset: String = "reset"
        override val undo: String = "undo"
        override val retry: String = "retry"
    }

    override val projectList = object : I18n.ProjectList {
        override val loadSampleProjects: String = "loadSampleProjects"
        override val noProjects: String = "noProjects"
        override val title: String = "title"
        override val addProject: String = "addProject"
        override val duration: String = "duration"
        override val projectDeleted: String = "projectDeleted"
        override val projectDeleteFailed: String = "projectDeleteFailed"
        override val projectRecreateFailed: String = "projectRecreateFailed"
    }

    override val projectCreate = object : I18n.ProjectCreate {
        override val title: String = "title"
        override val projectName: String = "projectName"
        override val invalidProjectName: String = "invalidProjectName"
        override val projectBpm: String = "projectBpm"
        override val invalidProjectBpm: String = "invalidProjectBpm"
        override val projectRhythm: String = "projectRhythm"
        override val createProjectButton: String = "createProjectButton"
        override val projectCreationError: String = "projectCreationError"
    }

    override val projectDetails = object : I18n.ProjectDetails {
        override val invalidTempo: String = "invalidTempo"
        override val tempo: String = "tempo"
        override val invalidChordBeats: String = "invalidChordBeats"
        override val invalidNoteBeats: String = "invalidNoteBeats"
        override val controlStateError: String = "controlStateError"
        override val projectSaveError: String = "projectSaveError"
        override val chordsTab: String = "chordsTab"
        override val melodyTab: String = "melodyTab"
        override val duration: String = "duration"
        override val octave: String = "octave"
        override val chordType: String = "chordType"
        override val velocity: String = "velocity"
        override val title: String = "title"

    }

    override val rhythm = object : I18n.Rhythm {
        override val fourFours: String = "fourFours"
        override val threeFours: String = "threeFours"
    }

    override val projectOptions = object : I18n.ProjectOptions {
        override val exportError: String = "exportError"
        override val title: String = "title"
        override val tempo: String = "tempo"
        override val tempoError: String = "tempoError"
        override val saveError: String = "saveError"
        override val export: String = "export"
        override val melodyPreset: String = "melodyPreset"
        override val chordsPreset: String = "chordsPreset"

    }

    override val projectAiGenerate = object : I18n.ProjectAiGenerate {
        override val unknownError: String = "unknownError"
        override val networkError: String = "networkError"
        override val parsingError: String = "parsingError"
        override val promptHint: String = "promptHint"
        override val promptOptions: String = "promptOptions"
        override val promptOptionChordsForMelody: String = "promptOptionChordsForMelody"
        override val promptOptionMelodyForChords: String = "promptOptionMelodyForChords"
        override val promptOptionChords: String = "promptOptionChords"
        override val promptOptionMelody: String = "promptOptionMelody"
    }
    override val chords = object : I18n.Chords {
        override val minor: String = "minor"
        override val major: String = "major"
        override val diminished: String = "diminished"
        override val augmented: String = "augmented"
        override val majorSeventh: String = "majorSeventh"
        override val minorSeventh: String = "minorSeventh"
        override val dominantSeventh: String = "dominantSeventh"
        override val halfDiminishedSeventh: String = "halfDiminishedSeventh"
        override val diminishedSeventh: String = "diminishedSeventh"
        override val augmentedSeventh: String = "augmentedSeventh"
        override val minorSixth: String = "minorSixth"
        override val majorSixth: String = "majorSixth"
    }
}