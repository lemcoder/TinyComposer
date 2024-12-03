import UIKit
import shared

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        TCApp.shared.start()

        window = UIWindow(frame: UIScreen.main.bounds)
    
        if let window = window {
            let uiController = UINavigationController( rootViewController: TCViewController.shared.getInitialScreen())
            uiController.interactivePopGestureRecognizer?.isEnabled = true
            window.rootViewController = uiController
           
            window.makeKeyAndVisible()
        }
        
        TCViewController.shared.start()
        return true
    }
}
