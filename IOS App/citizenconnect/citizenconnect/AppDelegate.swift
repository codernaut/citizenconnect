//
//  AppDelegate.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 23/02/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import RealmSwift
import Firebase
import UserNotifications
import Fabric
import Crashlytics
@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate{
    
    var window: UIWindow?
    
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        //Firebase
        FirebaseApp .configure()
        Messaging.messaging().delegate = self
        initRemoteNotification(application: application)
        Firebase.Notification.RegistrationToken = Messaging.messaging().fcmToken
        Messaging.messaging().shouldEstablishDirectChannel = true
        Fabric.with([Crashlytics.self])
        
        let backImage = UIImage(named: "back")?.withRenderingMode(.alwaysOriginal)
        UINavigationBar.appearance().backIndicatorImage = backImage
        UINavigationBar.appearance().backIndicatorTransitionMaskImage = backImage
        UINavigationBar.appearance().barStyle = .black
        
        application.statusBarStyle = .lightContent
        
        //Realm Configuration
        let config = Realm.Configuration(
            schemaVersion: UInt64(App.config.realmSchemaVersion),
            migrationBlock: { migration, oldSchemaVersion in
                // We haven’t migrated anything yet, so oldSchemaVersion == 0
                if (oldSchemaVersion < 1) {
                    // Nothing to do!
                    // Realm will automatically detect new properties and removed properties
                    // And will update the schema on disk automatically
                    }
            }
            
        )
        Realm.Configuration.defaultConfiguration = config
        if DataSet.getRealmData().count != 0 {
            let storyboard:UIStoryboard = UIStoryboard(name: "Main", bundle: Bundle.main);
            self.window?.rootViewController = storyboard.instantiateViewController(withIdentifier: "tabBarController");
            
            return true;
        }
        return true
    }
    
    func initRemoteNotification(application: UIApplication) {
        if #available(iOS 10.0, *) {
            // For iOS 10 display notification (sent via APNS)
            UNUserNotificationCenter.current().delegate = self
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: {_, _ in })
        }
        else {
            let settings: UIUserNotificationSettings =
                UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
        application.registerForRemoteNotifications()
    }
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken as Data
        Messaging.messaging().subscribe(toTopic: Firebase.Notification.ictNotificationTopic)
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }
    
    func applicationDidBecomeActive(_ application: UIApplication) {
        UNUserNotificationCenter.current().getDeliveredNotifications { (notifications) in
            for noti in notifications {
                if noti.request.content.title == "ICT Citizen Connect" {
                    
                }
            }
        }
    }

    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Registration failed!")
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // With swizzling disabled you must let Messaging know about the message, for Analytics
       // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        print(userInfo)
    }
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        
        if Auth.auth().canHandleNotification(userInfo) {
            completionHandler(.noData)
            return
        }
        
        //Will Trigger if you send Notification Message
        //Messaging.messaging().appDidReceiveMessage(userInfo)
        
        let state: UIApplicationState = UIApplication.shared.applicationState
        if state == .background {
       //     MyUtils.UpdateNotificationCount(index: 0)
       //     NotificationCenter.default.post(name: NSNotification.Name(rawValue: App.NotificationKeys.ictNotificiationKey), object: self)
            completionHandler(UIBackgroundFetchResult.newData)
        }
    }
    
}

@available(iOS 10, *)
extension AppDelegate : UNUserNotificationCenterDelegate {
    
    // Receive displayed notifications for iOS 10 devices.
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {

        MyUtils.UpdateNotificationCount(index: 0)
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: App.NotificationKeys.ictNotificiationKey), object: self)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        //This callback is no longer required
      //  print("\(response.notification.request.content.userInfo)")
        MyUtils.UpdateNotificationCount(index: 0)
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: App.NotificationKeys.ictNotificiationKey), object: self)
    }
    
}

extension AppDelegate : MessagingDelegate {

    func messaging(_ messaging: Messaging, didReceive remoteMessage: MessagingRemoteMessage) {
        //Will Trigger if you send Data Message
       // MyUtils.generateLocalNotification(title: "ICT Citizen Connect", subtitle: nil, body: "New update added click to view")
        MyUtils.UpdateNotificationCount(index: 0)
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: App.NotificationKeys.ictNotificiationKey), object: self)
        
    }
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String) {
        print("Firebase registration token: \(fcmToken)")
        // TODO: If necessary send token to application server.
        // Note: This callback is fired at each app startup and whenever a new token is generated.
    }
}

