//
//  VerifyPhoneController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 20/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit

class VerifyPhoneController: UIViewController {

    var verificationCode:Int!
    var verificationId:String!
    @IBAction func sendCode(_ sender: Any) {
    }
    @IBAction func verifyBtn(_ sender: Any) {
    }
    var phoneNumber:String!
    @IBOutlet weak var verifCode: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
            verifCode.keyboardType = UIKeyboardType.numberPad
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
