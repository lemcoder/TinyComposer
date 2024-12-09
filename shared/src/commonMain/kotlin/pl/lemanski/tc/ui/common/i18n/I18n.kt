package pl.lemanski.tc.ui.common.i18n

internal interface I18n {
    val common: Common
    val welcome: Welcome
    val projectList: ProjectList
    val projectCreate: ProjectCreate
    val projectDetails: ProjectDetails
    val rhythm: Rhythm
    val projectOptions: ProjectOptions
    val projectAiGenerate: ProjectAiGenerate

    interface Common {
        val ok: String
        val cancel: String
        val close: String
        val yes: String
        val no: String
        val confirm: String
        val delete: String
        val edit: String
        val add: String
        val save: String
        val reset: String
        val undo: String
        val retry: String
    }

    interface Welcome {
        val title: String
    }

    interface ProjectList {
        val title: String
        val addProject: String
        val duration: String
        val projectDeleted: String
        val projectDeleteFailed: String
        val projectRecreateFailed: String
    }

    interface ProjectCreate {
        val title: String
        val projectName: String
        val invalidProjectName: String
        val projectBpm: String
        val invalidProjectBpm: String
        val projectRhythm: String
        val createProjectButton: String
        val projectCreationError: String
    }

    interface Rhythm {
        val fourFours: String
        val threeFours: String
    }

    interface ProjectDetails {
        val invalidTempo: String?
        val tempo: String
        val invalidChordBeats: String
        val invalidNoteBeats: String
        val controlStateError: String
        val projectSaveError: String
        val chordsTab: String
        val melodyTab: String
        val duration: String
        val octave: String
        val chordType: String
        val velocity: String
        val title: String
    }

    interface ProjectOptions {
        val tempo: String
        val tempoError: String
        val saveError: String
        val export: String
        val melodyPreset: String
        val chordsPreset: String
    }
    
    interface ProjectAiGenerate {
        val unknownError: String
        val networkError: String
        val parsingError: String
        val promptHint: String
        val promptOptions: String
        val promptOptionChordsForMelody: String
        val promptOptionMelodyForChords: String
        val promptOptionChords: String
        val promptOptionMelody: String
    }
}