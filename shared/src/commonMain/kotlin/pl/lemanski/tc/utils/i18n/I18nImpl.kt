package pl.lemanski.tc.utils.i18n

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import tinycomposer.shared.generated.resources.Res
import tinycomposer.shared.generated.resources.*

internal class I18nImpl : I18n {

    private fun stringResourceBlocking(resource: StringResource): String = runBlocking { getString(resource) }

    override val common: I18n.Common by lazy { Common() }
    override val projectList: I18n.ProjectList by lazy { ProjectList() }


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

    }

    private inner class ProjectList : I18n.ProjectList {
        override val title: String = stringResourceBlocking(Res.string.project_list_title)
        override val addProject: String = stringResourceBlocking(Res.string.project_list_addProject)
    }
}