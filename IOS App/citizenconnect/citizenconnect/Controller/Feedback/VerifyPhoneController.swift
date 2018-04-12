//
//  VerifyPhoneController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 20/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import Firebase
import Alamofire
class VerifyPhoneController: UIViewController, UITextFieldDelegate {

    var verificationCode:String?
    var verificationId:String?
    var alert:UIAlertView!
    @IBOutlet weak var sendCode_Btn: UIButton!
    var result: ((Bool)->())?
    @IBOutlet weak var verify_Btn: UIButton!
    @IBOutlet weak var navBar: UINavigationBar!
    @IBAction func sendCode(_ sender: Any) {
        sendCode()
    }
    @IBOutlet weak var navItem: UINavigationItem!
    @IBAction func verifyBtn(_ sender: Any) {
        verfiyNo()
    }
    var phoneNumber:String?
    @IBOutlet weak var verifCode: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

        let cancelButton = UIBarButtonItem(title: "Cancel", style: .plain, target: self, action: #selector(VerifyPhoneController.dismissController(_:)))
        cancelButton.tintColor = UIColor.white
        navItem.leftBarButtonItem = cancelButton
        navItem.title = " "
        sendCode_Btn.layer.cornerRadius = 5
        verify_Btn.layer.cornerRadius = 5
        alert =   MyUtils.showAlert(title: nil, message: "Please wait", cancelBtnTitle: nil, sender: self)
        verifCode.keyboardType = UIKeyboardType.numberPad
        verificationId = UserDefaults.standard.string(forKey: "authVerificationID")
        phoneNumber = UserDefaults.standard.string(forKey: "userPhoneNo")
        verifCode.delegate = self

    }
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        let maxLength = 6
        let currentString: NSString = textField.text! as NSString
        let newString: NSString =
            currentString.replacingCharacters(in: range, with: string) as NSString
        return newString.length <= maxLength
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func verfiyNo(){
        alert.show()
        let credential = PhoneAuthProvider.provider().credential(
            withVerificationID: verificationId!,
            verificationCode: verifCode.text!)
        Auth.auth().signIn(with: credential) { (user, error) in
            self.view.endEditing(true)
            if (error != nil)  {
                print(error as Any)
                self.view.makeToast(error.debugDescription)
                 self.alert.dismiss(withClickedButtonIndex: -1, animated: true)
                return
            }
            else{
                self.result?(true)
                self.alert.dismiss(withClickedButtonIndex: -1, animated: true)
                self.navigationController?.popViewController(animated: true)
                self.dismiss(animated: true, completion: nil)
            }
        }
    }
    override var preferredStatusBarStyle : UIStatusBarStyle {
        return .lightContent
    }
    func sendCode(){
        alert.show()
        PhoneAuthProvider.provider().verifyPhoneNumber(phoneNumber!, uiDelegate: nil) { (verificationID, error) in
            if let error = error {
                print(error)
                self.alert.dismiss(withClickedButtonIndex: -1, animated: true)
                self.view.makeToast("Failed to verify")
                return
            }
            else {
                self.alert.dismiss(withClickedButtonIndex: -1, animated: true)
                self.verificationId = verificationID
            }
        }
    }
    @objc func dismissController(_ sender: AnyObject?){
         dismiss(animated: true, completion: nil)
    }

}
