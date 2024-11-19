package pl.lemanski.tc.ui

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.ui.common.i18n.I18nImpl
import pl.lemanski.tc.ui.projectCreate.ProjectCreateContract
import pl.lemanski.tc.ui.projectCreate.ProjectCreateViewModel
import pl.lemanski.tc.ui.projectsList.ProjectListViewModel
import pl.lemanski.tc.ui.projectsList.ProjectsListContract
import pl.lemanski.tc.ui.welcome.WelcomeContract
import pl.lemanski.tc.ui.welcome.WelcomeViewModel

internal object UiModule {
    fun provide() = module {
        singleOf(::I18nImpl) bind I18n::class

        viewModelOf(::WelcomeViewModel) bind WelcomeContract.ViewModel::class
        viewModelOf(::ProjectCreateViewModel) bind ProjectCreateContract.ViewModel::class
        viewModelOf(::ProjectListViewModel) bind ProjectsListContract.ViewModel::class
    }
}