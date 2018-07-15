//
//  PopUpDataFeedbackViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 30/05/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import Alamofire

class PopUpDataFeedbackViewController: UIViewController {

    @IBOutlet weak var cancel_PopUp: UIButton!
    @IBOutlet weak var container: UIView!
    @IBOutlet weak var buttonSendFeedback: UIButton!
    @IBOutlet weak var feedbackMessage: UITextView!
    
    var alert:UIAlertView!
    var type:String?
    var locationName :String?
    var locationAddress : String?
    override func viewDidLoad() {
        super.viewDidLoad()
        container.layer.cornerRadius = 5
        feedbackMessage.layer.cornerRadius = 5
        feedbackMessage.layer.borderWidth = 1.0
        feedbackMessage.layer.borderColor = UIColor(hexString: Colors.colorPrimary).cgColor
        buttonSendFeedback.layer.cornerRadius = 5
        cancel_PopUp.layer.cornerRadius = 5
        cancel_PopUp.layer.borderWidth = 1.0
        cancel_PopUp.layer.borderColor = UIColor(hexString: Colors.colorPrimary).cgColor
        
        let blurEffect = UIBlurEffect(style: UIBlurEffectStyle.dark)
        let blurEffectView = UIVisualEffectView(effect: blurEffect)
        blurEffectView.frame = view.frame
        blurEffectView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        self.view.insertSubview(blurEffectView, at: 0)
        
        alert =   MyUtils.showAlert(title: nil, message: "Please wait", cancelBtnTitle: nil, sender: self)
        
        buttonSendFeedback.addTarget(forControlEvents: .touchUpInside) { (button) in
            if (self.feedbackMessage.text?.isEmpty)! {
                self.view.makeToast("incomplete details")
            }
            else{
                self.alert =   MyUtils.showAlert(title: nil, message: "Sending", cancelBtnTitle: nil, sender: self)
                self.alert.show()
                let params:Parameters = [
                    "subject": self.type! ,
                    "message": self.feedbackMessage.text ?? String(),
                    "dataSetName": self.locationName! ,
                    "dataSetAddress": self.locationAddress!
                ]
                
                MyUtils.postData(url: ApiManager.sendComplain, parameters: params, completion: { (response) in
                    self.dismiss(animated: true, completion: nil)
                    self.view.makeToast("Sent")
                    self.alert.dismiss(withClickedButtonIndex: -1, animated: true)
                    print(response as Any)
                    
                })
            }
        }
        cancel_PopUp.addTarget(forControlEvents: .touchUpInside) { (button) in
            self.dismiss(animated: true, completion: nil)
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
