//
//  DataSet.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 13/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import Foundation
import RealmSwift
import UIKit
import ObjectMapper
import FirebaseDatabase

class DataSet: Object, Mappable {
    @objc dynamic var id  = UUID().uuidString
    @objc dynamic var dataSetType = ""
    @objc dynamic var address = ""
    @objc dynamic var name = ""
    @objc dynamic var latitude = 0.0
    @objc dynamic var longitude = 0.0
    @objc dynamic var mapAddress = ""
    
    required convenience init(map:Map) {
        self.init()
    }
    
    func mapping(map: Map) {
        dataSetType <- map["dataSetType"]
        address <- map["Address"]
        name <- map["Name"]
        latitude <- map["Latitude"]
        longitude <- map["Longitude"]
        mapAddress <- map["MapAddress"]
    }
    
    override static func primaryKey() -> String? {
        return "id"
    }
    
    public static func getRealmData() -> Results<DataSet>{
        let realm  = try! Realm()
        return realm.objects(DataSet.self)
    }
    
    public static func getDataSet(dataType:String) ->Results<DataSet> {
        let realm  = try! Realm()
        return realm.objects(DataSet.self).filter("dataSetType = '\(dataType)'")
    }
    
    public static func getDataSet(dataBaseReference: String,completion:@escaping (Bool,DataSnapshot) -> Void,fail: @escaping (Error?) ->Void){
        MyUtils.getFirebaseData(ref: Database.database().reference(withPath : dataBaseReference), completion: { (snapshot) in
                completion(true,snapshot)
        }) { (err) in
            
        }
    }
    
    static func searchDataList(query:String) ->Results<DataSet> {
        let realm = try! Realm()
        let predicate = NSPredicate(format: "name CONTAINS  [c] %@",query)
        return realm.objects(DataSet.self).filter(predicate)
    }
}
