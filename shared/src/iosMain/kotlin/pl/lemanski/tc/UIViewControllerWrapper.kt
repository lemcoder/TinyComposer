package pl.lemanski.tc

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.runBlocking
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.ui.common.TcViewModel
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.provide
import platform.Foundation.NSLog
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSStringFromClass
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIGestureRecognizerDelegateProtocol
import platform.UIKit.UISwipeGestureRecognizer
import platform.UIKit.UISwipeGestureRecognizerDirectionRight
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.navigationController
import platform.UIKit.navigationItem
import platform.UIKit.willMoveToParentViewController

/**
 * A custom `UIViewController` that wraps another `UIViewController` and adds gesture recognizer functionality.
 * Implements the `UIGestureRecognizerDelegateProtocol` to handle swipe gestures.
 *
 * @property controller The `UIViewController` instance that is being wrapped.
 */
class UIViewControllerWrapper(
    private val viewModel: TcViewModel<*>,
    private val controller: UIViewController,
) : UIViewController(null, null),
    UIGestureRecognizerDelegateProtocol {

    private val logger = Logger(this::class)
    private val navigationService: NavigationService = provide()

    /**
     * Called when the view controller's view is loaded into memory.
     * Sets up the view hierarchy by adding the wrapped controller's view as a subview
     * and managing the parent-child relationship between the view controllers.
     */
    @OptIn(ExperimentalForeignApi::class)
    override fun loadView() {
        super.loadView()
        controller.willMoveToParentViewController(this)
        controller.view.setFrame(view.frame)
        view.addSubview(controller.view)
        addChildViewController(controller)
        controller.didMoveToParentViewController(this)
    }

    /**
     * Called after the view has been loaded.
     * Sets the delegate for the interactive pop gesture recognizer and adds swipe gesture recognizers
     * for left and right swipe directions.
     */
    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLoad() {
        super.viewDidLoad()
        logger.debug("View did load -------------- ")
        logger.error(viewModel.hashCode().toString())
        viewModel.onAttached()

        navigationItem.hidesBackButton = true

        controller.navigationController?.interactivePopGestureRecognizer?.enabled = true
        controller.navigationController?.interactivePopGestureRecognizer?.delaysTouchesBegan = true
        controller.navigationController?.interactivePopGestureRecognizer?.delegate = this
//        controller.navigationController?.interactivePopGestureRecognizer?.let { popRecognizer ->
//            popRecognizer.getAllGestureRecognizers(controller.view).filter {
//                NSStringFromClass(it.`class`()!!) == "CMPGestureRecognizer"
//            }.forEach {
//                it.requireGestureRecognizerToFail(popRecognizer)
//            }
//        }

        val gesture = UISwipeGestureRecognizer(
            target = UISwipeGestureRecognizerDirectionRight,
            action = NSSelectorFromString("handleSwipe:")
        )
        gesture.direction = UISwipeGestureRecognizerDirectionRight
        gesture.delegate = this
        this.view.addGestureRecognizer(gesture)
    }

    /**
     * Handles the swipe gestures detected by the gesture recognizers.
     * Logs the direction of the swipe.
     *
     * @param sender The `UISwipeGestureRecognizer` that detected the swipe.
     */
    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun handleSwipe(sender: UISwipeGestureRecognizer) {
        if (sender.direction == UISwipeGestureRecognizerDirectionRight) {
            runBlocking {
                navigationService.back()
            }
        }

        NSLog("Swipe detected: ${sender.direction}")
    }

    /**
     * Determines whether the gesture recognizer should begin interpreting touches.
     * Always returns `true`.
     *
     * @param gestureRecognizer The `UIGestureRecognizer` that is asking whether it should begin.
     * @return `true` to allow the gesture recognizer to begin.
     */
    override fun gestureRecognizerShouldBegin(gestureRecognizer: UIGestureRecognizer): Boolean {
        return true
    }

    /**
     * Determines whether the gesture recognizer should be required to fail by another gesture recognizer.
     * Always returns `true`.
     *
     * @param gestureRecognizer The `UIGestureRecognizer` that is asking whether it should be required to fail.
     * @param shouldBeRequiredToFailByGestureRecognizer The `UIGestureRecognizer` that is requiring the failure.
     * @return `true` to require the gesture recognizer to fail.
     */
    override fun gestureRecognizer(
        gestureRecognizer: UIGestureRecognizer,
        shouldBeRequiredToFailByGestureRecognizer: UIGestureRecognizer
    ): Boolean {
        return true
    }
}