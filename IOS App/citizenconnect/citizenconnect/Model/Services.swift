//
//  Services.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 07/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import Foundation
import RealmSwift
import UIKit
import ObjectMapper


class Services : Object,Mappable{
    
    @objc dynamic var name = ""
    @objc dynamic var fileUrl = ""
    
    required convenience init(map:Map) {
        self.init()
    }
    
    func mapping(map: Map) {
        name <- map["data_set_name"]
        fileUrl <- map["fileUrl"]
    }
    
   
    
    static func getRealmData() ->Results<Services> {
        let realm = try! Realm()
        return realm.objects(Services.self)
    }
    
    static func getFilePath(serviceName:String) ->String {
        let realm = try! Realm()
        let service:Services = realm.objects(Services.self).filter("name = '\(serviceName)'").first!
        return service.fileUrl
    }
}
