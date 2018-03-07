//
//  Layout.swift
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

class Layout : Object , Mappable {
    
    @objc dynamic var id = ""
    @objc dynamic var color = ""
    @objc dynamic var icon = ""
    @objc dynamic var name  = ""
    
    required convenience init(map:Map) {
        self.init()
    }
    
    func mapping(map: Map) {
        id  <- map["id"]
        color <- map["color"]
        icon <- map["icon"]
        name <- map ["data_set_name"]
    }
    
    public static func getLayout(dataBaseReference: String,type: String) {
        let realm = try! Realm()
        var layoutObjects = getRealmData()
        if layoutObjects.count == 0 {
            
        }else{
            
        }
    }
    
    static func getRealmData() ->Results<Layout> {
        let realm = try! Realm()
        return realm.objects(Layout.self)
    }
}
