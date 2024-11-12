package pl.lemanski.tc.utils.i18n

internal interface I18n {
    val common: Common
    val projectList: ProjectList

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

    interface ProjectList {
        val title: String
        val addProject: String
    }
}