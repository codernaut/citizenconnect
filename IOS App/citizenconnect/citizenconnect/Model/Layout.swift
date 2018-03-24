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
    
    @objc dynamic var id = UUID().uuidString
    @objc dynamic var color = ""
    @objc dynamic var icon = ""
    @objc dynamic var name  = ""
    @objc dynamic var type = ""
    
    required convenience init(map:Map) {
        self.init()
    }
    
    func mapping(map: Map) {
        color <- map["color"]
        icon <- map["icon"]
        name <- map ["data_set_name"]
    }
    
    override static func primaryKey() -> String? {
        return "id"
    }
    
    public static func getLayout(dataBaseReference: String,Datatype: String,completion: @escaping (Results<Layout>) -> Void,fail: @escaping (Error?) -> Void) {
        let realm = try! Realm()
        let layoutObjects = getRealmData(type: Datatype)
        if layoutObjects.count == 0 {
            MyUtils.getFirebaseData(ref: Database.database().reference(withPath : dataBaseReference), completion: { (snapshot) in
                let mSnapshot  = snapshot.children
                while let layoutObject =  mSnapshot.nextObject() as? DataSnapshot {
                    if Datatype=="services" {
                        // map services files to service object here
                        let services = Mapper<Services>().map(JSONObject: layoutObject.value)
                        try! realm.write{
                            realm.add(services!)
                        }
                    }
                    let layout =  Mapper<Layout>().map(JSONObject: layoutObject.value)
                    layout?.type = Datatype
                    try! realm.write{
                        realm.add(layout!)
                    }
                }
                completion(layoutObjects)
            }, failed: { (err) in
                fail(err)
            })
        }else{
            completion(layoutObjects)
        }
    }
    
    static func getRealmData(type: String) ->Results<Layout> {
        let realm = try! Realm()
        return realm.objects(Layout.self).filter("type = '\(type)'")
    }
}
