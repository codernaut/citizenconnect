//
//  MyUtils.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 07/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import Foundation
import RealmSwift
import FirebaseDatabase
import UIKit
import Alamofire

class MyUtils {
    
    static func getFirebaseData(ref: DatabaseReference!, completion: @escaping (DataSnapshot)->Void,failed: @escaping (Error?)->Void){
    
        ref?.observeSingleEvent(of: .value, with: { (snapshot) in
           
            completion(snapshot)
            
        },withCancel: {(err) in
            
            failed(err)
            
        })
    }
    
    static func NotificationbadgeCount (sender: AnyObject!,index:Int){
        if let tabItems = sender!.tabBarController??.tabBar.items as NSArray!
        {
            let tabItem = tabItems[index] as! UITabBarItem
            switch(index){
            case(0):
                let realm = try! Realm()
                let status = realm.objects(NotificationStatus.self).first
                if status?.notificationCount != nil{
                    tabItem.badgeValue = status?.notificationCount
                }
            default:
                print("xdsds")
            }
        }
    }
    static func UpdateNotificationCount (index:Int) {
        let realm = try! Realm()
        let status = realm.objects(NotificationStatus.self).first
        if status != nil {
            let count:Int  = Int((status?.notificationCount)!)! + 1
            try! realm.write {
                 status?.notificationCount = String(count)
            }
        }
        else
        {   let obj = NotificationStatus()
            obj.notificationCount = "1"
           try! realm.write {
                realm.add(obj)
            }
        }
    }
    static func NotificationbadgeClear (sender: AnyObject!,index:Int){
        if let tabItems = sender!.tabBarController??.tabBar.items as NSArray!
        {
            let tabItem = tabItems[index] as! UITabBarItem
            switch(index){
            case(0):
                let realm = try! Realm()
                let status = realm.objects(NotificationStatus.self).first
                try! realm.write{
                    status?.notificationCount = "0"
                }
                tabItem.badgeValue = nil
            default:
                print("default")
            }
        }
    }
    
    func heightForView(text:String, font:UIFont, width:CGFloat) -> CGFloat{
        let label:UILabel = UILabel(frame: CGRect(origin: CGPoint(x: 0, y: 0), size: CGSize(width: width, height: CGFloat.greatestFiniteMagnitude)))
        label.numberOfLines = 0
        label.lineBreakMode = NSLineBreakMode.byWordWrapping
        label.font = font
        label.text = text
        
        label.sizeToFit()
        return label.frame.height
    }
    public static func showAlert(title:String!,message:String!,cancelBtnTitle:String!,sender:AnyObject) -> UIAlertView {
        let alert: UIAlertView = UIAlertView(title: title, message: message, delegate: nil, cancelButtonTitle: cancelBtnTitle);
        let rect = CGRect(origin: CGPoint(x: 50,y :10), size: CGSize(width: 37, height: 37))
        let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: rect) as UIActivityIndicatorView
        loadingIndicator.center = sender.view.center;
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
        loadingIndicator.startAnimating();
        
        alert.setValue(loadingIndicator, forKey: "accessoryView")
        loadingIndicator.startAnimating()
        loadingIndicator.startAnimating()
        return alert
    }
    public static func postData(url:String,headers: HTTPHeaders, parameters: Parameters,completion: @escaping (AnyObject?) -> Void){
        Alamofire.request(url, method: .post, parameters: parameters ,headers: headers).responseJSON { (response) in
            completion(response as AnyObject)
        }
    }
}
extension UIColor {
    convenience init(hexString: String, alpha: CGFloat = 1.0) {
        let hexString: String = hexString.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
        let scanner = Scanner(string: hexString)
        if (hexString.hasPrefix("#")) {
            scanner.scanLocation = 1
        }
        var color: UInt32 = 0
        scanner.scanHexInt32(&color)
        let mask = 0x000000FF
        let r = Int(color >> 16) & mask
        let g = Int(color >> 8) & mask
        let b = Int(color) & mask
        let red   = CGFloat(r) / 255.0
        let green = CGFloat(g) / 255.0
        let blue  = CGFloat(b) / 255.0
        self.init(red:red, green:green, blue:blue, alpha:alpha)
    }
    func toHexString() -> String {
        var r:CGFloat = 0
        var g:CGFloat = 0
        var b:CGFloat = 0
        var a:CGFloat = 0
        getRed(&r, green: &g, blue: &b, alpha: &a)
        let rgb:Int = (Int)(r*255)<<16 | (Int)(g*255)<<8 | (Int)(b*255)<<0
        return String(format:"#%06x", rgb)
    }
}
extension UIImage {
    
    func maskWithColor(color: UIColor) -> UIImage? {
        let maskImage = cgImage!
        
        let width = size.width
        let height = size.height
        let bounds = CGRect(x: 0, y: 0, width: width, height: height)
        
        let colorSpace = CGColorSpaceCreateDeviceRGB()
        let bitmapInfo = CGBitmapInfo(rawValue: CGImageAlphaInfo.premultipliedLast.rawValue)
        let context = CGContext(data: nil, width: Int(width), height: Int(height), bitsPerComponent: 8, bytesPerRow: 0, space: colorSpace, bitmapInfo: bitmapInfo.rawValue)!
        
        context.clip(to: bounds, mask: maskImage)
        context.setFillColor(color.cgColor)
        context.fill(bounds)
        
        if let cgImage = context.makeImage() {
            let coloredImage = UIImage(cgImage: cgImage)
            return coloredImage
        } else {
            return nil
        }
    }
    
}
