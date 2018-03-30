//
//  Firebase.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 28/02/2018.
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
    }
}
