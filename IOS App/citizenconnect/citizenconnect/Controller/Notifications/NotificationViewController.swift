//
//  FirstViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 23/02/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import FirebaseDatabase
import RealmSwift
import SDWebImage

class NotificationViewController: UIViewController,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout,UICollectionViewDelegate  {
    var notificationObjects = [Notification]()
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = "Notifications"
        let menuButton = UIBarButtonItem(image: UIImage(named: "menuIcon"), style: .plain, target: self, action: Selector("action"))
        menuButton.tintColor = UIColor.white
        self.navigationItem.rightBarButtonItem = menuButton
        Notification.getNotificationList(completion: { (results) in
            for notification in results {
                self.notificationObjects.append(notification)
            }

        }) { (error) in
            
        }
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        let kWhateverHeightYouWant = 110.0
        return CGSize(width: collectionView.bounds.size.width-20.0, height: CGFloat(kWhateverHeightYouWant))
    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return notificationObjects.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "notificationLayout", for: indexPath) as! NotificationCollectionViewCell
        cell.imageView.sd_setImage(with: URL(string: notificationObjects[indexPath.row].filePath),placeholderImage:#imageLiteral(resourceName: "placeHolder"))
        cell.notificationDescription.text = notificationObjects[indexPath.row].notificationDescription
        cell.notificationDate.text = notificationObjects[indexPath.row].date
        
        
        cell.backgroundColor = UIColor.white
        cell.contentView.layer.cornerRadius = 4.0
        cell.contentView.layer.borderWidth = 1.0
        cell.contentView.layer.borderColor = UIColor.clear.cgColor
        cell.contentView.layer.masksToBounds = false
        cell.layer.shadowColor = UIColor.gray.cgColor
        cell.layer.shadowOffset = CGSize(width: 0, height: 1.0)
        cell.layer.shadowRadius = 4.0
        cell.layer.shadowOpacity = 1.0
        cell.layer.masksToBounds = false
        cell.layer.shadowPath = UIBezierPath(roundedRect: cell.bounds, cornerRadius: cell.contentView.layer.cornerRadius).cgPath
        
        return cell
    }
}


