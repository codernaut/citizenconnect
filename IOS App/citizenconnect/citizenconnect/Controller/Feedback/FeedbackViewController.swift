//
//  FeedbackViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 15/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import Toast_Swift
import Firebase
import Popover


class FeedbackViewController: UIViewController,UIPickerViewDelegate, UIPickerViewDataSource {

    var picker: UIPickerView!
    @IBOutlet weak var categoryTV: UITextField!
    @IBOutlet weak var fullNameTV: UITextField!
    @IBOutlet weak var message: UITextView!
    var popover:Popover!
    fileprivate var texts = ["About us"]
    @IBOutlet weak var phoneTV: UITextField!
    var keys: [String] = [String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        view.addGestureRecognizer(tap)
        phoneTV.keyboardType = UIKeyboardType.phonePad
        self.navigationItem.title = "Feedback"
        self.navigationItem.backBarButtonItem?.title = ""
        let menuButton = UIBarButtonItem(image: UIImage(named: "menuIcon"), style: .plain, target: self, action: #selector(showMenu))
        menuButton.tintColor = UIColor.white
        let emergencyCallButton  = UIBarButtonItem(image: UIImage(named: "phone_filled"), style: .plain, target: self, action: #selector(emergencyCall))
        emergencyCallButton.tintColor = UIColor.white
        self.navigationItem.rightBarButtonItem = menuButton
        // self.navigationItem.setRightBarButtonItems([menuButton, emergencyCallButton], animated: true)
        message.layer.borderWidth = 0.5
        message .layer.cornerRadius = 7.0
        message.layer.borderColor = UIColor.lightGray.cgColor
        categoryTV.endEditing(true)
        keys = ["Traffic Issues" , "Government Issues" , "Others"]
        picker = UIPickerView(frame: CGRect(origin: .zero, size: CGSize(width: 200, height: 220)))
        picker.backgroundColor = UIColor.white
        
        picker.showsSelectionIndicator = true
        picker.delegate = self
        picker.dataSource = self
        let toolBar = UIToolbar()
        toolBar.barStyle = UIBarStyle.default
        toolBar.isTranslucent = true
        toolBar.tintColor = UIColor(red: 76/255, green: 217/255, blue: 100/255, alpha: 1)
        toolBar.sizeToFit()
        
    
        let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.plain, target: self, action: #selector(FeedbackViewController.donePicker))
        doneButton.tintColor = UIColor(hexString: "#26a69a")
        let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: nil, action: nil)
        let cancelButton = UIBarButtonItem(title: "Cancel", style: UIBarButtonItemStyle.plain, target: self, action: #selector(FeedbackViewController.donePicker))
        cancelButton.tintColor = UIColor(hexString: "#26a69a")
        
        toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
        toolBar.isUserInteractionEnabled = true
        
        categoryTV.inputView = picker
        categoryTV.inputAccessoryView = toolBar
        
    }

    
    @objc func showMenu() ->Void {
        let tableView = UITableView(frame: CGRect(x: 0, y: 0, width: self.view.frame.width/2, height: 35))
        tableView.delegate = self
        tableView.dataSource = self
        let startPoint = CGPoint(x: self.view.frame.width - 20, y: 55)
        popover = Popover()
        popover.show(tableView, point: startPoint)
    }
    
    @IBAction func sendMessage(_ sender: Any) {
        if Validate() == false {
            self.view.makeToast("Please provide all details")
        }
        else {
            PhoneAuthProvider.provider().verifyPhoneNumber(phoneTV.text!, uiDelegate: nil) { (verificationID, error) in
                if let error = error {
                  print(error)
                    self.view.makeToast("Failed to verify")
                    return
                }
                else {
                    let storyboard = UIStoryboard(name: "Main", bundle: nil)
                    var controller = storyboard.instantiateViewController(withIdentifier: "verifyCode") as! VerifyPhoneController
                    controller.phoneNumber = self.phoneTV.text
                    controller.verificationId = verificationID
                    
                    self.present(controller, animated: true, completion: nil)
                }
            }
        }
    }
    
    func Validate() -> Bool {
        var valid:Bool = true
        if (fullNameTV.text?.isEmpty)! {
           /*  fullNameTV.attributedPlaceholder = NSAttributedString(string: "Please enter Full name", attributes: [NSAttributedStringKey.foregroundColor: UIColor.red])*/
            valid = false
        }
        if (phoneTV.text?.isEmpty)!{
            /*phoneTV.attributedPlaceholder = NSAttributedString(string: "Please enter Phone Number", attributes: [NSAttributedStringKey.foregroundColor: UIColor.red])*/
            valid = false
        }
        if (categoryTV.text?.isEmpty)!{
           /* phoneTV.attributedPlaceholder = NSAttributedString(string: "Please select Category", attributes: [NSAttributedStringKey.foregroundColor: UIColor.red])*/
            valid = false
        }
        if (message.text?.isEmpty)!{
          /*  message.attributedPlaceholder = NSAttributedString(string: "Please enter your Message", attributes: [NSAttributedStringKey.foregroundColor: UIColor.red])*/
            valid = false
        }
        return valid
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return keys.count
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return keys[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        categoryTV.text = keys[row]
    }
    
   @objc func donePicker()-> Void{
        categoryTV.resignFirstResponder()
    }
    
    @objc func emergencyCall()->Void {
        
    }
    
    @objc func dismissKeyboard() ->Void {
        //Causes the view (or one of its embedded text fields) to resign the first responder status.
        view.endEditing(true)
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
extension FeedbackViewController: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.popover.dismiss()
        performSegue(withIdentifier: "aboutUs", sender: self)
    }
}

extension FeedbackViewController: UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        return 1
    }

    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .default, reuseIdentifier: nil)
        cell.textLabel?.text = self.texts[(indexPath as NSIndexPath).row]
        return cell
    }
}
