//
//  Notification.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 28/02/2018.
//  Copyright © 2018 cfp. All rights reserved.
//


import Foundation
import RealmSwift
import FirebaseDatabase
import UIKit
import ObjectMapper

class Notification :  Object,Mappable{
    @objc dynamic var id = ""
    @objc dynamic var filePath = ""
    @objc dynamic var date = ""
    @objc dynamic var notificationDescription = ""
    @objc dynamic var tag = ""
    
    required convenience init?(map: Map) {
        self.init()
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        filePath <- map["filePath"]
        date <- map["date"]
        notificationDescription <- map["description"]
        tag <- map["tag"]
    }
    
    public static func getNotificationList(completion: @escaping (Results<Notification>) -> Void, fail: @escaping (Error?) -> Void) {
        let realm = try! Realm()
        let notificationList = getRealmData()
        if notificationList.count == 0 {
            MyUtils.getFirebaseData(ref: Database.database().reference(withPath : Firebase.Database.NotificationPath), completion: { (snapshot) in
                let mSnapshot  = snapshot.children
                while let notificationObject =  mSnapshot.nextObject() as? DataSnapshot {
                    let notification =  Mapper<Notification>().map(JSONObject: notificationObject.value)
                    notification?.id = notificationObject.key
                    try! realm.write{
                        realm.add(notification!)
                    }
                }
                completion(notificationList)
            }) { (err) in
                fail(err)
            }
        }
        else {
            completion(notificationList)
        }
    }
    
    static func clearNotifications() -> Void {
        let realm = try! Realm()
        try! realm.write {
            realm.delete(getRealmData())
        }
    }
    
    static func getRealmData() ->Results<Notification> {
        let realm = try! Realm()
        return realm.objects(Notification.self)
    }
    static func searchNotificationList(query:String) ->Results<Notification> {
        let realm = try! Realm()
        let predicate = NSPredicate(format: "notificationDescription CONTAINS  [c] %@",query)
        return realm.objects(Notification.self).filter(predicate)
    }
}
