//
//  MyUtils.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 07/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import Foundation
import RealmSwift
import FirebaseDatabase

class MyUtils {
    
    static func getFirebaseData(ref: DatabaseReference!, completion: @escaping (DataSnapshot)->Void,failed: @escaping (Error?)->Void){
    
        ref?.observeSingleEvent(of: .value, with: { (snapshot) in
           
            completion(snapshot)
            
        },withCancel: {(err) in
            
            failed(err)
            
        })
    }
}
