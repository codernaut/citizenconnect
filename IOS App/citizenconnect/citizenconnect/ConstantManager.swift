//
//  ConstantManager.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 09/05/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import Foundation
struct Firebase {
    struct Database {
        static let NotificationPath = "Notifications"
        static let LayoutServices = "project/services"
        static let LayoutDataSets = "project/data_sets"
        static let DataSetsPath = "dataset"
    }
    struct Notification {
        static var RegistrationToken:String?
        static let ictNotificationTopic = "/topics/iphoneNotification"
    }
    static var mapAPI_KEY = "AIzaSyC2W93Fm9CirXA6zWvQfTs2M6HrH4ZuZO0"
}
struct ApiManager {
    //----- Production ---------//
    static let sendfeedBack = "https://us-central1-citizenconnect-ed5fa.cloudfunctions.net/openEndpoints/sendEmail"
    static let sendComplain = "https://us-central1-citizenconnect-ed5fa.cloudfunctions.net/openEndpoints/sendComplain"
    
    //----- Stagging ----------//
    //static let sendComplain = "https://us-central1-zero-point-stagging.cloudfunctions.net/openEndpoints/sendComplain"
    //static let sendfeedBack = "https://us-central1-zero-point-stagging.cloudfunctions.net/openEndpoints/sendEmail"
}
struct App {
    struct NotificationKeys {
        static let ictNotificiationKey = "update"
    }
    struct config {
        static let realmSchemaVersion = 4
    }
}
struct Colors {
    static let colorPrimary = "#26a69a"
    static let colorPrimaryDark = ""
    static let darkGrey = "#C4C4C4"
}
