//
//  BaseViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 12/06/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit

class BaseViewController: UIViewController,UISearchBarDelegate {
    var menuButton:UIBarButtonItem!
    var emergencyCallButton:UIBarButtonItem!
    var searchButton:UIBarButtonItem!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Add menu Item
        menuButton = UIBarButtonItem(image: UIImage(named: "info"), style: .plain, target: self, action: #selector(showMenu))
        menuButton.tintColor = UIColor.white
        
        emergencyCallButton  = UIBarButtonItem(image: UIImage(named: "phone_filled"), style: .plain, target: self, action: #selector(emergencyCall))
        emergencyCallButton.tintColor = UIColor.white
        self.navigationItem.rightBarButtonItem = menuButton
        self.navigationItem.setRightBarButtonItems([menuButton, emergencyCallButton], animated: true)
        UINavigationBar.appearance().tintColor = UIColor.white
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func showMenu() ->Void {
        performSegue(withIdentifier: "aboutUs", sender: self)
    }
    
    @objc func emergencyCall() ->Void {
        performSegue(withIdentifier: "popUpEmergencyCalls", sender: self)
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
        self.navigationItem.rightBarButtonItems = nil
    }
    func addSearchButton() -> Void {
        searchButton  = UIBarButtonItem(image: UIImage(named: "search"), style: .plain, target: self, action: #selector(addSearchBar))
        searchButton.tintColor = UIColor.white
        self.navigationItem.leftBarButtonItem = searchButton
    }

}
