package pl.lemanski.tc.ui.common.i18n

internal interface I18n {
    val common: Common
    val welcome: Welcome
    val projectList: ProjectList
    val projectCreate: ProjectCreate

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
    }

    interface Welcome {
        val title: String
    }

    interface ProjectList {
        val title: String
        val addProject: String
        val duration: String
    }

    interface ProjectCreate {
        val title: String
        val projectName: String
        val projectBpm: String
        val projectRhythm: String
        val createProjectButton: String
    }
}