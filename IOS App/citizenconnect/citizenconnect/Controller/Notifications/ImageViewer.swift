//
//  ImageViewer.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 16/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import Alamofire
import ImageScrollView

class ImageViewer: UIViewController {
    var imagePath:String = ""
    var notificationDescription: String?
    @IBOutlet weak var descriptionLabel: UILabel?
    var imageView: UIImageView?
    @IBOutlet weak var imageShow: ImageScrollView!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        self.navigationController?.navigationBar.shadowImage = UIImage()
        self.navigationController?.navigationBar.isTranslucent = true
        self.navigationController?.view.backgroundColor = .clear
        
        let cancelButton = UIBarButtonItem(image: UIImage(named: "cancel"), style: .plain, target: self, action: #selector(ImageViewer.dismissController(_:)))
        cancelButton.tintColor = UIColor.white
        self.navigationItem.leftBarButtonItem = cancelButton
        
        Alamofire.request(imagePath).responseImage { response in
            if let image:UIImage = response.result.value {
                self.imageShow.display(image: image)
            }
        }
        descriptionLabel?.text = notificationDescription
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated
    }
    
    @objc func dismissController(_ sender: AnyObject?) {
        dismiss(animated: true, completion: nil)
    }
    override var prefersStatusBarHidden: Bool{
        return true
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
