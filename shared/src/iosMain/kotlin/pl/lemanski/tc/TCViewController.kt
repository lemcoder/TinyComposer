package pl.lemanski.tc

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.CreationExtras
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.viewmodel.resolveViewModel
import pl.lemanski.tc.TCViewController.start
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.NavigationEvent
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.ui.common.localViewModel
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsRouter
import pl.lemanski.tc.ui.projectAiGenerate.ProjectAiGenerateContract
import pl.lemanski.tc.ui.projectAiGenerate.ProjectAiGenerateRouter
import pl.lemanski.tc.ui.projectCreate.ProjectCreateContract
import pl.lemanski.tc.ui.projectCreate.ProjectCreateRouter
import pl.lemanski.tc.ui.projectOptions.ProjectOptionsContract
import pl.lemanski.tc.ui.projectOptions.ProjectOptionsRouter
import pl.lemanski.tc.ui.projectsList.ProjectListRouter
import pl.lemanski.tc.ui.projectsList.ProjectsListContract
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.MaterialThemeWrapper
import platform.UIKit.UIApplication
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.childViewControllers
import platform.UIKit.navigationController

/**
 * The object that handles navigation on iOS
 * currently it works like that:
 * - it listens to the navigation events from the [NavigationService] (but has to be initialized in the [start] method) on Native side
 * - it gets the top view controller from the view hierarchy
 * - it pushes the new view controller to the navigation stack when the [NavigationEvent.Direction.FORWARD] is received
 * - it pops the top view controller from the navigation stack when the [NavigationEvent.Direction.BACKWARD] is received
 * - it provides the first view controller to be displayed (the initial screen)
 *
 * The view model lifecycle is handled by the [UIViewControllerWrapper] class
 * as opposed to the Android side where it is handled by compose lifecycle (LaunchedEffect)
 * This is an ugly workaround to make the view models lifecycle work on iOS
 *
 * The view models are provided by the [ServiceLocator] class which is not ideal TODO: refactor
 */

object TCViewController : KoinComponent {
    private val logger = Logger(this::class)

    private val navigationService: NavigationService by inject()

    private fun goto(navController: UINavigationController, destination: Destination) {
        val viewController = destination.getViewController()
        navController.interactivePopGestureRecognizer?.enabled = true
        navController.delegate = viewController as? UINavigationControllerDelegateProtocol
        navController.pushViewController(viewController, animated = true)
    }

    private fun back(navController: UINavigationController) {
        if (navController.viewControllers.size > 1) {
            navController.popViewControllerAnimated(true)
        } else {
            logger.warn("Cannot pop. Only one view controller in the stack.")
        }
    }

    // Used in iOSApp
    fun getInitialScreen(): UIViewController {
        return ProjectListDestination.getViewController()
    }

    fun start() {
        val navigationController = getNavigationController() ?: run {
            logger.error("NavigationController is null")
            return
        }

        navigationService.setOnNavigateListener { event ->
            when (event.direction) {
                NavigationEvent.Direction.FORWARD -> goto(navigationController, event.destination)
                NavigationEvent.Direction.BACKWARD -> back(navigationController)
            }
        }
    }

    // --- View controllers

    private fun projectListViewController(viewModel: ProjectsListContract.ViewModel) =
        UIViewControllerWrapper(
            viewModel = viewModel,
            controller = ComposeUIViewController {
                MaterialThemeWrapper {
                    CompositionLocalProvider(
                        localViewModel provides viewModel
                    ) {
                        ProjectListRouter()
                    }
                }
            }
        )

    private fun projectCreateViewController(viewModel: ProjectCreateContract.ViewModel) =
        UIViewControllerWrapper(
            viewModel = viewModel,
            controller = ComposeUIViewController {
                MaterialThemeWrapper {
                    CompositionLocalProvider(
                        localViewModel provides viewModel,
                    ) {
                        ProjectCreateRouter()
                    }
                }
            }
        )

    private fun projectDetailsViewController(viewModel: ProjectDetailsContract.ViewModel) =
        UIViewControllerWrapper(
            viewModel = viewModel,
            controller = ComposeUIViewController {
                MaterialThemeWrapper {
                    CompositionLocalProvider(
                        localViewModel provides viewModel
                    ) {
                        ProjectDetailsRouter()
                    }
                }
            }
        )

    private fun projectAiGenerateViewController(viewModel: ProjectAiGenerateContract.ViewModel) =
        UIViewControllerWrapper(
            viewModel = viewModel,
            controller = ComposeUIViewController {
                MaterialThemeWrapper {
                    CompositionLocalProvider(
                        localViewModel provides viewModel
                    ) {
                        ProjectAiGenerateRouter()
                    }
                }
            }
        )

    private fun projectOptionsViewController(viewModel: ProjectOptionsContract.ViewModel) =
        UIViewControllerWrapper(
            viewModel = viewModel,
            controller = ComposeUIViewController {
                MaterialThemeWrapper {
                    CompositionLocalProvider(
                        localViewModel provides viewModel,
                    ) {
                        ProjectOptionsRouter()
                    }
                }
            }
        )

    // ---

    @OptIn(KoinInternalApi::class)
    private fun Destination.getViewController(): UIViewController {

        return when (this) {
            ProjectListDestination -> projectListViewController(
                resolveViewModel(
                    vmClass = ProjectsListContract.ViewModel::class,
                    viewModelStore = this.viewModelStore,
                    parameters = { parametersOf(this) },
                    extras = CreationExtras.Empty,
                    scope = TCApp.koinInstance.koin.scopeRegistry.rootScope // TODO: refactor
                )
            )

            ProjectCreateDestination -> projectCreateViewController(
                resolveViewModel(
                    vmClass = ProjectCreateContract.ViewModel::class,
                    viewModelStore = this.viewModelStore,
                    parameters = { parametersOf(this) },
                    extras = CreationExtras.Empty,
                    scope = TCApp.koinInstance.koin.scopeRegistry.rootScope // TODO: refactor
                )
            )

            is ProjectDetailsDestination -> projectDetailsViewController(
                resolveViewModel(
                    vmClass = ProjectDetailsContract.ViewModel::class,
                    viewModelStore = this.viewModelStore,
                    parameters = { parametersOf(this) },
                    extras = CreationExtras.Empty,
                    scope = TCApp.koinInstance.koin.scopeRegistry.rootScope // TODO: refactor
                )
            )

            is ProjectAiGenerateDestination -> projectAiGenerateViewController(
                resolveViewModel(
                    vmClass = ProjectAiGenerateContract.ViewModel::class,
                    viewModelStore = this.viewModelStore,
                    parameters = { parametersOf(this) },
                    extras = CreationExtras.Empty,
                    scope = TCApp.koinInstance.koin.scopeRegistry.rootScope // TODO: refactor
                )
            )

            is ProjectOptionsDestination -> projectOptionsViewController(
                resolveViewModel(
                    vmClass = ProjectOptionsContract.ViewModel::class,
                    viewModelStore = this.viewModelStore,
                    parameters = { parametersOf(this) },
                    extras = CreationExtras.Empty,
                    scope = TCApp.koinInstance.koin.scopeRegistry.rootScope // TODO: refactor
                )
            )
        }
    }
}

fun getNavigationController(): UINavigationController? {
    val topVc = getTopViewController()
    return topVc?.let { topViewController ->
        topViewController as? UINavigationController
            ?: topViewController.navigationController
    }
}

/**
 * Retrieves the top `UIViewController` from the view hierarchy.
 *
 * @param base The base `UIViewController` to start the search from. Defaults to the root view controller of the key window.
 * @return The top `UIViewController`, or null if none is found.
 */
fun getTopViewController(base: UIViewController? = UIApplication.sharedApplication().keyWindow?.rootViewController): UIViewController? {
    when {
        base is UINavigationController -> {
            return getTopViewController(base = base.visibleViewController)
        }

        base is UITabBarController -> {
            return getTopViewController(base = base.selectedViewController)
        }

        base?.presentedViewController != null -> {
            return getTopViewController(base = base.presentedViewController)
        }

        base.toString().contains("HostingController") -> return getTopViewController(
            base = base?.childViewControllers()?.first() as UIViewController
        )

        else -> {
            return base
        }
    }
}

///**
// * Logs the hierarchy of the top `UIViewController` for debugging purposes.
// *
// * @param base The base `UIViewController` to start the search from. Defaults to the root view controller of the key window.
// */
//fun debugTopViewController(base: UIViewController? = UIApplication.sharedApplication().keyWindow?.rootViewController) {
//    if (base is UINavigationController) {
//        NSLog("TopViewController: UINavigationController with visible view controller: ${base.visibleViewController}")
//        debugTopViewController(base = base.visibleViewController)
//    } else if (base is UITabBarController) {
//        NSLog("TopViewController: UITabBarController with selected view controller: ${base.selectedViewController}")
//        debugTopViewController(base = base.selectedViewController)
//    } else if (base?.presentedViewController != null) {
//        NSLog("TopViewController: Presented view controller: ${base.presentedViewController}")
//        debugTopViewController(base = base.presentedViewController)
//    } else {
//        NSLog("TopViewController: ${base}")
//    }
//}
