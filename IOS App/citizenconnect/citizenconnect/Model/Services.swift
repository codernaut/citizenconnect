//
//  Services.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 07/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import Foundation
import RealmSwift
import FirebaseDatabase
import UIKit
import ObjectMapper


class Services : Object,Mappable{
    
    @objc dynamic var id = ""
    @objc dynamic var type = ""
    @objc dynamic var fileUrl = ""
    
    required convenience init(map:Map) {
        self.init()
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        type <- map["type"]
        fileUrl <- map["fileUrl"]
    }
    
    public static func getNotificationList(completion: @escaping (Bool,Results<Services>) -> Void) {
        let realm = try! Realm()
    }
    
    static func getRealmData() ->Results<Services> {
        let realm = try! Realm()
        return realm.objects(Services.self)
    }
}
