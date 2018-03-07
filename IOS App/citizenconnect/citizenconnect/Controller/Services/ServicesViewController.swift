//
//  ServicesViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 07/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit

class ServicesViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = "Services"
        let menuButton = UIBarButtonItem(image: UIImage(named: "menuIcon"), style: .plain, target: self, action: Selector("action"))
        menuButton.tintColor = UIColor.white
        self.navigationItem.rightBarButtonItem = menuButton
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
