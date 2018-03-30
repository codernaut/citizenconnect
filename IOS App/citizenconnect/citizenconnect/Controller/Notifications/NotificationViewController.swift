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
import AlamofireImage
import  Alamofire
import Popover



class NotificationViewController: UIViewController,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout,UICollectionViewDelegate,UISearchBarDelegate, UIGestureRecognizerDelegate, delegateNotificationCV  {

    var notificationObjects = [Notification]()
    var SpinnerView:UIView!
    @IBOutlet weak var NotificationCollectionView: UICollectionView!
    var menuButton:UIBarButtonItem!
    var searchButton:UIBarButtonItem!
    var emergencyCallButton:UIBarButtonItem!
    var popover:Popover!
    fileprivate var texts = ["About us"]
    var mSegue:UIStoryboardSegue!
    var imageArr = [UIImage]()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let notificationView = NotificationCollectionViewCell()
        notificationView.delegate = self
        NotificationCollectionView.delegate = self
        NotificationCollectionView.dataSource = self
        SpinnerView = UIViewController.displaySpinner(onView: self.view)
        self.navigationItem.title = "Notifications"
        addMenuButton()
        addSearchButton()
        initializeData()
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
            self.mSegue = segue
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
        cell.rowAt = indexPath.row
        cell.delegate = self
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
        
        imageArr.append(cell.imageView.image!)
        return cell
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        initializeData()
        NotificationCollectionView.reloadData()
        searchBar.showsCancelButton = false
        searchBar.isHidden = true
        self.navigationItem.title = "Notifications"
        self.navigationItem.titleView = nil
        //self.navigationItem.setRightBarButtonItems([menuButton, emergencyCallButton], animated: true)
        //self.navigationItem.setRightBarButton(menuButton, animated: true)
        addMenuButton()
        addSearchButton()

    }

   
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        search(searchText: searchText)
    }
    /*
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
     
    }
    */
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        initializeData()
        NotificationCollectionView.reloadData()
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.search(searchText: searchBar.text!)
    }
    
    @objc func addSearchBar() -> Void {
        let searchBar = UISearchBar()
        searchBar.setShowsCancelButton(true, animated: true)
        searchBar.placeholder = "Enter your search here"
        searchBar.delegate = self
        let cancelButtonAttributes = [NSAttributedStringKey.foregroundColor: UIColor.white]
        UIBarButtonItem.appearance().setTitleTextAttributes(cancelButtonAttributes , for: .normal)
        self.navigationItem.titleView = searchBar
        self.navigationItem.leftBarButtonItem = nil
        self.navigationItem.rightBarButtonItem = nil
    }
    
    @objc func emergencyCall() ->Void {
        let popup = UIStoryboard(name: "Main",bundle: nil).instantiateViewController(withIdentifier: "popUpView") as! PopUpViewController
        self.addChildViewController(popup)
        popup.view.frame = self.view.frame
        self.view.addSubview(popup.view)
        popup.didMove(toParentViewController: self)
    }
    @objc func showMenu() ->Void {
        let tableView = UITableView(frame: CGRect(x: 0, y: 0, width: self.view.frame.width/2, height: 35))
        tableView.delegate = self
        tableView.dataSource = self
        let startPoint = CGPoint(x: self.view.frame.width - 10, y: 55)
        popover = Popover()
        popover.show(tableView, point: startPoint)
    }
    
    func addMenuButton() -> Void {
        
        menuButton = UIBarButtonItem(image: UIImage(named: "menuIcon"), style: .plain, target: self, action: #selector(showMenu))
        menuButton.tintColor = UIColor.white
        
        emergencyCallButton  = UIBarButtonItem(image: UIImage(named: "phone_filled"), style: .plain, target: self, action: #selector(emergencyCall))
        emergencyCallButton.tintColor = UIColor.white
        self.navigationItem.rightBarButtonItem = menuButton
       // self.navigationItem.setRightBarButtonItems([menuButton, emergencyCallButton], animated: true)
        
        
    }
    func addSearchButton() -> Void {
        searchButton  = UIBarButtonItem(image: UIImage(named: "search"), style: .plain, target: self, action: #selector(addSearchBar))
        searchButton.tintColor = UIColor.white
        self.navigationItem.leftBarButtonItem = searchButton
    }
    
    func search(searchText:String) -> Void {
        if searchText.count>0 {
            let results = Notification.searchNotificationList(query: searchText)
            notificationObjects.removeAll()
            for notification in results {
                notificationObjects.append(notification)
            }
            NotificationCollectionView.reloadData()
        }
    }
    
    func viewFullImage(imageAt: Int) {
        if mSegue.identifier == "imageViewNav" {
            let imageNC = mSegue.destination as! NotificationNC
            let imageVC =  imageNC.viewControllers.first as! ImageViewer
            imageVC.imagePath = notificationObjects[imageAt].filePath
            imageVC.notificationDescription = notificationObjects[imageAt].notificationDescription
            performSegue(withIdentifier: "imageViewNav",
                         sender: self)
        }
    }
    
    func shareImage(imageAt: Int) {
        let alert:UIAlertView! =   MyUtils.showAlert(title: nil, message: "Please wait", cancelBtnTitle: nil, sender: self)
        alert.show()
        Alamofire.request(notificationObjects[imageAt].filePath).responseImage { response in
            if let image:UIImage = response.result.value {
                let imageToShare = [ image]
                let activityViewController = UIActivityViewController(activityItems: imageToShare, applicationActivities: nil)
                activityViewController.popoverPresentationController?.sourceView = self.view
                alert.dismiss(withClickedButtonIndex: -1, animated: true) 
                self.present(activityViewController, animated: true, completion: nil)
            }
        }
    }
    
    func initializeData(){
        MyUtils.NotificationbadgeCount(sender: self, index: 0)
        self.notificationObjects.removeAll()
        Notification.getNotificationList(completion: { (results) in
            for notification in results {
                self.notificationObjects.append(notification)
            }
            self.NotificationCollectionView.reloadData()
            UIViewController.removeSpinner(spinner: self.SpinnerView)
            
        }) { (error) in
            
        }
    }
}

extension NotificationViewController: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.popover.dismiss()
         performSegue(withIdentifier: "aboutUs", sender: self)
    }
}

extension NotificationViewController: UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .default, reuseIdentifier: nil)
        cell.textLabel?.text = self.texts[(indexPath as NSIndexPath).row]
        return cell
    }
}


