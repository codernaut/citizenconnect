//
//  NotificationCollectionViewCell.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 02/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit

class NotificationCollectionViewCell: UICollectionViewCell {
    weak var delegate: delegateNotificationCV?
    var rowAt: Int!

    @IBOutlet weak var notificationDescription: UILabel!
    @IBOutlet weak var notificationDate: UILabel!
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var viewBtn: UIButton!
    @IBOutlet weak var imageShare: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        viewBtn.addTarget(self, action: #selector(NotificationCollectionViewCell.showImageAt(_:)), for: .touchUpInside)
        imageShare.addTarget(self, action:#selector(NotificationCollectionViewCell.shareImage(_:)), for: .touchUpInside)
        
    }
    
    @objc func showImageAt(_ sender: AnyObject?){
        delegate?.viewFullImage(imageAt: rowAt)
    }
    @objc func shareImage(_ sender: AnyObject?){
        delegate?.shareImage(imageAt: rowAt)
    }
    
}

protocol delegateNotificationCV: NSObjectProtocol {
    func viewFullImage(imageAt:Int)
    func shareImage(imageAt:Int)
}

