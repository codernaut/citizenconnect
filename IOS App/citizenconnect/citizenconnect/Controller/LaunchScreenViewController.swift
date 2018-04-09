//
//  LaunchScreenViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 13/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import MBCircularProgressBar
import FirebaseDatabase
import ObjectMapper
import RealmSwift

class LaunchScreenViewController: UIViewController {

    @IBOutlet weak var labelProgress: UIActivityIndicatorView!
    @IBOutlet weak var progressLabel: UILabel!
    @IBOutlet weak var progressView: MBCircularProgressBarView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        labelProgress.startAnimating()
        self.progressView.value = 0
        self.progressView.maxValue = 100
        
        if DataSet.getRealmData().count == 0 {
            fetchDataFromFirebase()
        }
        else{
            DispatchQueue.main.async{
                [unowned self] in
                self.performSegue(withIdentifier: "tabBarController", sender: self)
            }
            
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        UIView.animate(withDuration: 1.0) {
            if self.progressView.value <= 100{
                self.progressView.value += 0.002
            }
        }
    }
    
    private func fetchDataFromFirebase() ->Void {
        DataSet.getDataSet(dataBaseReference: Firebase.Database.DataSetsPath, completion: {(Status,snapShot) in
            DispatchQueue.global(qos: .userInteractive).async {
                DispatchQueue.main.async {
                    self.progressView.value = 2
                    self.progressLabel.text = "Downloading Updates"
                }
                let realm = try! Realm()
                for datasets in snapShot.children {
                    let mSnapShot = (datasets as! DataSnapshot).childSnapshot(forPath: "data")
                    let dataSetType = (datasets as! DataSnapshot).childSnapshot(forPath: "type").value
                    for data in mSnapShot.children {
                        let object = Mapper<DataSet>().map(JSONObject: (data as! DataSnapshot).value)
                        object?.dataSetType = dataSetType as! String
                        try! realm.write {
                            realm.add(object!)
                        }
                        DispatchQueue.main.async
                            {
                                if self.progressView.value == 100 {
                                    self.progressLabel.text = "Almost Done"
                                }
                                else{
                                    self.viewWillAppear(true)
                                }
                        }
                    }
                }
                self.performSegue(withIdentifier: "tabBarController", sender: self)
            }
            
            
        }) { (err) in
            
        }
    }
}
