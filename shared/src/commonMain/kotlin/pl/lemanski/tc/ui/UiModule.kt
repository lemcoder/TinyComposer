package pl.lemanski.tc.ui

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.ui.common.i18n.I18nImpl
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsViewModel
import pl.lemanski.tc.ui.projectAiGenerate.ProjectAiGenerateContract
import pl.lemanski.tc.ui.projectAiGenerate.ProjectAiGenerateViewModel
import pl.lemanski.tc.ui.projectCreate.ProjectCreateContract
import pl.lemanski.tc.ui.projectCreate.ProjectCreateViewModel
import pl.lemanski.tc.ui.projectOptions.ProjectOptionsContract
import pl.lemanski.tc.ui.projectOptions.ProjectOptionsViewModel
import pl.lemanski.tc.ui.projectsList.ProjectListViewModel
import pl.lemanski.tc.ui.projectsList.ProjectsListContract

internal object UiModule {
    fun provide() = module {
        singleOf(::I18nImpl) bind I18n::class

        viewModel<ProjectCreateContract.ViewModel> { (key: ProjectCreateDestination) -> ProjectCreateViewModel(key, get(), get(), get()) }
        viewModel<ProjectsListContract.ViewModel> { (key: ProjectListDestination) -> ProjectListViewModel(key, get(), get(), get(), get(), get()) }
        viewModel<ProjectDetailsContract.ViewModel> { (key: ProjectDetailsDestination) -> ProjectDetailsViewModel(key, get(), get(), get(), get(), get(), get(), get()) }
        viewModel<ProjectOptionsContract.ViewModel> { (key: ProjectOptionsDestination) -> ProjectOptionsViewModel(key, get(), get(), get(), get(), get(), get()) }
        viewModel<ProjectAiGenerateContract.ViewModel> { (key: ProjectAiGenerateDestination) -> ProjectAiGenerateViewModel(key, get(), get(), get(), get()) }
    }
}