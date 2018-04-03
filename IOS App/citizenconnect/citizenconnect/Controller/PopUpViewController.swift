//
//  PopUpViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 20/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit

class PopUpViewController: UIViewController {

    @IBOutlet weak var fireExtView: UIView!
    @IBOutlet weak var ambulanceView: UIView!
    @IBOutlet weak var policeView: UIView!
    @IBOutlet weak var ambulanceImage: UIImageView!
    
    @IBOutlet weak var container: UIView!
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var fireExtImage: UIImageView!
    @IBOutlet weak var policeImage: UIImageView!
    
    var uiImage:UIImage?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        cancelButton.layer.cornerRadius = 5
        container.layer.cornerRadius = 5

        let blurEffect = UIBlurEffect(style: UIBlurEffectStyle.dark)
        let blurEffectView = UIVisualEffectView(effect: blurEffect)
        blurEffectView.frame = view.frame
        blurEffectView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        self.view.insertSubview(blurEffectView, at: 0)
        
        policeImage.image = policeImage.image?.maskWithColor(color: UIColor(hexString: "#26a69a"))
        ambulanceImage.image = ambulanceImage.image?.maskWithColor(color: UIColor(hexString: "#26a69a"))
        fireExtImage.image = fireExtImage.image?.maskWithColor(color: UIColor(hexString: "#26a69a"))
        
        let callPolice = UITapGestureRecognizer(target: self, action: #selector(self.callPoliceTapped(_:)))
        let callAmbulance = UITapGestureRecognizer(target: self, action: #selector(self.callAmbulanceTapped(_:)))
        let callFireBrigade = UITapGestureRecognizer(target: self, action: #selector(self.callFireBrigadeTapped(_:)))
        ambulanceView.addGestureRecognizer(callAmbulance)
        fireExtView.addGestureRecognizer(callFireBrigade)
        policeView.addGestureRecognizer(callPolice)
    }
    @IBAction func action_Cancel(_ sender: Any) {
         dismiss(animated: true, completion: nil)
    }
    @objc func callPoliceTapped(_ sender: UITapGestureRecognizer?) {
        UIApplication.shared.open(NSURL(string: "tel://05115")! as URL, options: [:], completionHandler: nil)
    }
    
    @objc func callAmbulanceTapped(_ sender: UITapGestureRecognizer?) {
        UIApplication.shared.open(NSURL(string: "tel://051115")! as URL, options: [:], completionHandler: nil)
    }
    @objc func callFireBrigadeTapped(_ sender: UITapGestureRecognizer?) {
        UIApplication.shared.open(NSURL(string: "tel://05116")! as URL, options: [:], completionHandler: nil)
    }
}
