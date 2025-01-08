package pl.lemanski.tc.ui.common.i18n

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import tinycomposer.shared.generated.resources.Res
import tinycomposer.shared.generated.resources.chord_type_augmented
import tinycomposer.shared.generated.resources.chord_type_augmented_seventh
import tinycomposer.shared.generated.resources.chord_type_diminished
import tinycomposer.shared.generated.resources.chord_type_diminished_seventh
import tinycomposer.shared.generated.resources.chord_type_dominant_seventh
import tinycomposer.shared.generated.resources.chord_type_half_diminished_seventh
import tinycomposer.shared.generated.resources.chord_type_major
import tinycomposer.shared.generated.resources.chord_type_major_seventh
import tinycomposer.shared.generated.resources.chord_type_major_sixth
import tinycomposer.shared.generated.resources.chord_type_minor
import tinycomposer.shared.generated.resources.chord_type_minor_seventh
import tinycomposer.shared.generated.resources.chord_type_minor_sixth
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
import tinycomposer.shared.generated.resources.project_ai_generate_network_error
import tinycomposer.shared.generated.resources.project_ai_generate_parsing_error
import tinycomposer.shared.generated.resources.project_ai_generate_prompt_hint
import tinycomposer.shared.generated.resources.project_ai_generate_prompt_option_chords
import tinycomposer.shared.generated.resources.project_ai_generate_prompt_option_chords_for_melody
import tinycomposer.shared.generated.resources.project_ai_generate_prompt_option_melody
import tinycomposer.shared.generated.resources.project_ai_generate_prompt_option_melody_for_chords
import tinycomposer.shared.generated.resources.project_ai_generate_prompt_options
import tinycomposer.shared.generated.resources.project_ai_generate_unknown_error
import tinycomposer.shared.generated.resources.project_create_invalid_project_bpm
import tinycomposer.shared.generated.resources.project_create_invalid_project_name
import tinycomposer.shared.generated.resources.project_create_project_bpm
import tinycomposer.shared.generated.resources.project_create_project_creation_error
import tinycomposer.shared.generated.resources.project_create_project_name
import tinycomposer.shared.generated.resources.project_create_project_rhythm
import tinycomposer.shared.generated.resources.project_create_title
import tinycomposer.shared.generated.resources.project_details_chord_type
import tinycomposer.shared.generated.resources.project_details_chords_tab
import tinycomposer.shared.generated.resources.project_details_control_state_error
import tinycomposer.shared.generated.resources.project_details_duration
import tinycomposer.shared.generated.resources.project_details_invalid_chord_beats
import tinycomposer.shared.generated.resources.project_details_invalid_note_beats
import tinycomposer.shared.generated.resources.project_details_invalid_tempo
import tinycomposer.shared.generated.resources.project_details_melody_tab
import tinycomposer.shared.generated.resources.project_details_octave
import tinycomposer.shared.generated.resources.project_details_project_save_error
import tinycomposer.shared.generated.resources.project_details_tempo
import tinycomposer.shared.generated.resources.project_details_title
import tinycomposer.shared.generated.resources.project_details_velocity
import tinycomposer.shared.generated.resources.project_list_add_project
import tinycomposer.shared.generated.resources.project_list_duration
import tinycomposer.shared.generated.resources.project_list_load_sample_projects
import tinycomposer.shared.generated.resources.project_list_no_projects
import tinycomposer.shared.generated.resources.project_list_project_delete_failed
import tinycomposer.shared.generated.resources.project_list_project_deleted
import tinycomposer.shared.generated.resources.project_list_project_recreate_failed
import tinycomposer.shared.generated.resources.project_list_title
import tinycomposer.shared.generated.resources.project_options_chords_preset
import tinycomposer.shared.generated.resources.project_options_export
import tinycomposer.shared.generated.resources.project_options_export_error
import tinycomposer.shared.generated.resources.project_options_melody_preset
import tinycomposer.shared.generated.resources.project_options_save_error
import tinycomposer.shared.generated.resources.project_options_tempo
import tinycomposer.shared.generated.resources.project_options_tempo_error
import tinycomposer.shared.generated.resources.project_options_title
import tinycomposer.shared.generated.resources.rhythm_four_fours
import tinycomposer.shared.generated.resources.rhythm_three_fours

internal class I18nImpl : I18n {

    private fun stringResourceBlocking(resource: StringResource): String = runBlocking { getString(resource) }

    override val common: I18n.Common by lazy { Common() }
    override val projectList: I18n.ProjectList by lazy { ProjectList() }
    override val projectCreate: I18n.ProjectCreate by lazy { ProjectCreate() }
    override val projectDetails: I18n.ProjectDetails by lazy { ProjectDetails() }
    override val rhythm: I18n.Rhythm by lazy { Rhythm() }
    override val projectOptions: I18n.ProjectOptions by lazy { ProjectOptions() }
    override val projectAiGenerate: I18n.ProjectAiGenerate by lazy { ProjectAiGenerate() }
    override val chords: I18n.Chords by lazy { Chords() }

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
        override val loadSampleProjects: String = stringResourceBlocking(Res.string.project_list_load_sample_projects)
        override val noProjects: String = stringResourceBlocking(Res.string.project_list_no_projects)
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
        override val invalidTempo: String = stringResourceBlocking(Res.string.project_details_invalid_tempo)
        override val tempo: String = stringResourceBlocking(Res.string.project_details_tempo)
        override val invalidChordBeats: String = stringResourceBlocking(Res.string.project_details_invalid_chord_beats)
        override val invalidNoteBeats: String = stringResourceBlocking(Res.string.project_details_invalid_note_beats)
        override val controlStateError: String = stringResourceBlocking(Res.string.project_details_control_state_error)
        override val projectSaveError: String = stringResourceBlocking(Res.string.project_details_project_save_error)
        override val chordsTab: String = stringResourceBlocking(Res.string.project_details_chords_tab)
        override val melodyTab: String = stringResourceBlocking(Res.string.project_details_melody_tab)
        override val duration: String = stringResourceBlocking(Res.string.project_details_duration)
        override val octave: String = stringResourceBlocking(Res.string.project_details_octave)
        override val chordType: String = stringResourceBlocking(Res.string.project_details_chord_type)
        override val velocity: String = stringResourceBlocking(Res.string.project_details_velocity)
        override val title: String = stringResourceBlocking(Res.string.project_details_title)
    }

    private inner class ProjectOptions : I18n.ProjectOptions {
        override val export: String = stringResourceBlocking(Res.string.project_options_export)
        override val melodyPreset: String = stringResourceBlocking(Res.string.project_options_melody_preset)
        override val chordsPreset: String = stringResourceBlocking(Res.string.project_options_chords_preset)
        override val exportError: String = stringResourceBlocking(Res.string.project_options_export_error)
        override val title: String = stringResourceBlocking(Res.string.project_options_title)
        override val tempo: String = stringResourceBlocking(Res.string.project_options_tempo)
        override val tempoError: String = stringResourceBlocking(Res.string.project_options_tempo_error)
        override val saveError: String = stringResourceBlocking(Res.string.project_options_save_error)
    }

    private inner class ProjectAiGenerate : I18n.ProjectAiGenerate {
        override val unknownError: String = stringResourceBlocking(Res.string.project_ai_generate_unknown_error)
        override val networkError: String = stringResourceBlocking(Res.string.project_ai_generate_network_error)
        override val parsingError: String = stringResourceBlocking(Res.string.project_ai_generate_parsing_error)
        override val promptHint: String = stringResourceBlocking(Res.string.project_ai_generate_prompt_hint)
        override val promptOptions: String = stringResourceBlocking(Res.string.project_ai_generate_prompt_options)
        override val promptOptionChordsForMelody: String = stringResourceBlocking(Res.string.project_ai_generate_prompt_option_chords_for_melody)
        override val promptOptionMelodyForChords: String = stringResourceBlocking(Res.string.project_ai_generate_prompt_option_melody_for_chords)
        override val promptOptionChords: String = stringResourceBlocking(Res.string.project_ai_generate_prompt_option_chords)
        override val promptOptionMelody: String = stringResourceBlocking(Res.string.project_ai_generate_prompt_option_melody)
    }

    private inner class Chords : I18n.Chords {
        override val minor: String = stringResourceBlocking(Res.string.chord_type_minor)
        override val major: String = stringResourceBlocking(Res.string.chord_type_major)
        override val diminished: String = stringResourceBlocking(Res.string.chord_type_diminished)
        override val augmented: String = stringResourceBlocking(Res.string.chord_type_augmented)
        override val majorSeventh: String = stringResourceBlocking(Res.string.chord_type_major_seventh)
        override val minorSeventh: String = stringResourceBlocking(Res.string.chord_type_minor_seventh)
        override val dominantSeventh: String = stringResourceBlocking(Res.string.chord_type_dominant_seventh)
        override val halfDiminishedSeventh: String = stringResourceBlocking(Res.string.chord_type_half_diminished_seventh)
        override val diminishedSeventh: String = stringResourceBlocking(Res.string.chord_type_diminished_seventh)
        override val augmentedSeventh: String = stringResourceBlocking(Res.string.chord_type_augmented_seventh)
        override val minorSixth: String = stringResourceBlocking(Res.string.chord_type_minor_sixth)
        override val majorSixth: String = stringResourceBlocking(Res.string.chord_type_major_sixth)
    }
}