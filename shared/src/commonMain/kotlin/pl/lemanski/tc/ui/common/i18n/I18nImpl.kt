package pl.lemanski.tc.ui.common.i18n

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import tinycomposer.shared.generated.resources.Res
import tinycomposer.shared.generated.resources.common_add
import tinycomposer.shared.generated.resources.common_cancel
import tinycomposer.shared.generated.resources.common_close
import tinycomposer.shared.generated.resources.common_confirm
import tinycomposer.shared.generated.resources.common_delete
import tinycomposer.shared.generated.resources.common_edit
import tinycomposer.shared.generated.resources.common_no
import tinycomposer.shared.generated.resources.common_ok
import tinycomposer.shared.generated.resources.common_reset
import tinycomposer.shared.generated.resources.common_retry
import tinycomposer.shared.generated.resources.common_save
import tinycomposer.shared.generated.resources.common_undo
import tinycomposer.shared.generated.resources.common_yes
import tinycomposer.shared.generated.resources.project_create_invalid_project_bpm
import tinycomposer.shared.generated.resources.project_create_invalid_project_name
import tinycomposer.shared.generated.resources.project_create_project_bpm
import tinycomposer.shared.generated.resources.project_create_project_creation_error
import tinycomposer.shared.generated.resources.project_create_project_name
import tinycomposer.shared.generated.resources.project_create_project_rhythm
import tinycomposer.shared.generated.resources.project_create_title
import tinycomposer.shared.generated.resources.project_list_add_project
import tinycomposer.shared.generated.resources.project_list_duration
import tinycomposer.shared.generated.resources.project_list_project_delete_failed
import tinycomposer.shared.generated.resources.project_list_project_deleted
import tinycomposer.shared.generated.resources.project_list_project_recreate_failed
import tinycomposer.shared.generated.resources.project_list_title
import tinycomposer.shared.generated.resources.rhythm_four_fours
import tinycomposer.shared.generated.resources.rhythm_three_fours
import tinycomposer.shared.generated.resources.welcome_title

internal class I18nImpl : I18n {

    private fun stringResourceBlocking(resource: StringResource): String = runBlocking { getString(resource) }

    override val common: I18n.Common by lazy { Common() }
    override val projectList: I18n.ProjectList by lazy { ProjectList() }
    override val projectCreate: I18n.ProjectCreate by lazy { ProjectCreate() }
    override val projectDetails: I18n.ProjectDetails by lazy { ProjectDetails() }
    override val welcome: I18n.Welcome by lazy { Welcome() }
    override val rhythm: I18n.Rhythm by lazy { Rhythm() }
    override val projectOptions: I18n.ProjectOptions by lazy { ProjectOptions() }
    override val projectAiGenerate: I18n.ProjectAiGenerate by lazy { ProjectAiGenerate() }

    private inner class Welcome : I18n.Welcome {
        override val title: String = stringResourceBlocking(Res.string.welcome_title)
    }

    private inner class Common : I18n.Common {
        override val ok: String = stringResourceBlocking(Res.string.common_ok)
        override val cancel: String = stringResourceBlocking(Res.string.common_cancel)
        override val close: String = stringResourceBlocking(Res.string.common_close)
        override val yes: String = stringResourceBlocking(Res.string.common_yes)
        override val no: String = stringResourceBlocking(Res.string.common_no)
        override val confirm: String = stringResourceBlocking(Res.string.common_confirm)
        override val delete: String = stringResourceBlocking(Res.string.common_delete)
        override val edit: String = stringResourceBlocking(Res.string.common_edit)
        override val add: String = stringResourceBlocking(Res.string.common_add)
        override val save: String = stringResourceBlocking(Res.string.common_save)
        override val reset: String = stringResourceBlocking(Res.string.common_reset)
        override val undo: String = stringResourceBlocking(Res.string.common_undo)
        override val retry: String = stringResourceBlocking(Res.string.common_retry)
    }

    private inner class ProjectList : I18n.ProjectList {
        override val title: String = stringResourceBlocking(Res.string.project_list_title)
        override val addProject: String = stringResourceBlocking(Res.string.project_list_add_project)
        override val duration: String = stringResourceBlocking(Res.string.project_list_duration)
        override val projectDeleted: String = stringResourceBlocking(Res.string.project_list_project_deleted)
        override val projectDeleteFailed: String = stringResourceBlocking(Res.string.project_list_project_delete_failed)
        override val projectRecreateFailed: String = stringResourceBlocking(Res.string.project_list_project_recreate_failed)
    }

    private inner class ProjectCreate : I18n.ProjectCreate {
        override val title: String = stringResourceBlocking(Res.string.project_create_title)
        override val projectName: String = stringResourceBlocking(Res.string.project_create_project_name)
        override val invalidProjectName: String = stringResourceBlocking(Res.string.project_create_invalid_project_name)
        override val projectBpm: String = stringResourceBlocking(Res.string.project_create_project_bpm)
        override val invalidProjectBpm: String = stringResourceBlocking(Res.string.project_create_invalid_project_bpm)
        override val projectRhythm: String = stringResourceBlocking(Res.string.project_create_project_rhythm)
        override val createProjectButton: String = stringResourceBlocking(Res.string.common_save)
        override val projectCreationError: String = stringResourceBlocking(Res.string.project_create_project_creation_error)
    }

    private inner class Rhythm : I18n.Rhythm {
        override val fourFours: String = stringResourceBlocking(Res.string.rhythm_four_fours)
        override val threeFours: String = stringResourceBlocking(Res.string.rhythm_three_fours)
    }

    private inner class ProjectDetails : I18n.ProjectDetails {
        override val invalidTempo: String = "invalid tempo"
        override val tempo: String = " tempo"
        override val invalidChordBeats: String = "invalid chord beats"
        override val invalidNoteBeats: String = "invalid note beats"
        override val controlStateError: String = "Control state error"
        override val projectSaveError: String = "Project save error"
        override val chordsTab: String = "Chords"
        override val melodyTab: String = "Melody"
        override val duration: String = "duration"
        override val octave: String = "octave"
        override val chordType: String = "chord type"
        override val velocity: String = "velocity"
        override val title: String = " Project details"
    }

    private inner class ProjectOptions : I18n.ProjectOptions {
        override val export: String = "Export"
        override val melodyPreset: String = "Melody preset"
        override val chordsPreset: String = "Chords preset"
        override val tempo: String = "Tempo"
        override val tempoError: String = "Bad tempo"
        override val saveError: String = "Save error"
    }

    private inner class ProjectAiGenerate : I18n.ProjectAiGenerate {
        override val unknownError: String = "unknownError"
        override val networkError: String = "networkError"
        override val parsingError: String = "parsingError"
        override val promptHint: String = "Prompt hint"
        override val promptOptions: String = "Prompt options"
        override val promptOptionChordsForMelody: String = "promptOptionChordsForMelody"
        override val promptOptionMelodyForChords: String = "promptOptionMelodyForChords"
        override val promptOptionChords: String = "promptOptionChords"
        override val promptOptionMelody: String = "promptOptionMelody"
    }
}