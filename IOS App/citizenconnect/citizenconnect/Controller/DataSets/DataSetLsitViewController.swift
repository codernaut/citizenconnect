//
//  DataSetLsitViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 14/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit

class DataSetLsitViewController: UIViewController,UITableViewDataSource ,UITableViewDelegate,UISearchBarDelegate
{
    @IBOutlet weak var dataTable: UITableView!
    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var DataSetListView: UITableView!
    var dataType:String = ""
    var dataSetobjects = [DataSet]()
    override func viewDidLoad() {
        super.viewDidLoad()
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        view.addGestureRecognizer(tap)
        let cancelButton = UIBarButtonItem(image: UIImage(named: "back"), style: .plain, target: self, action: #selector(ImageViewer.dismissController(_:)))
        cancelButton.tintColor = UIColor.white
        self.navigationItem.leftBarButtonItem = cancelButton
        let cancelButtonAttributes = [NSAttributedStringKey.foregroundColor: UIColor(hexString: "#26a69a")]
        UIBarButtonItem.appearance().setTitleTextAttributes(cancelButtonAttributes , for: .normal)
        self.navigationItem.backBarButtonItem?.title = ""
        self.navigationItem.title = dataType
        initializeData()
        
    }
    
    @objc func dismissController(_ sender: AnyObject?) {
        dismiss(animated: true, completion: nil)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSetobjects.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "dataSetCell", for: indexPath) as! DataSetsTableViewCell
        cell.Name.text = dataSetobjects[indexPath.row].name
        cell.Address.text = dataSetobjects[indexPath.row].address
        return cell
    }

    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        initializeData()
        dataTable.reloadData()
        searchBar.text = ""
        self.searchBar.setShowsCancelButton(false, animated: true)
        view.endEditing(true)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        search(searchText: searchText)
    }

     func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        self.searchBar.setShowsCancelButton(true, animated: true)
     }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        self.searchBar.setShowsCancelButton(true, animated: true)
        initializeData()
        dataTable.reloadData()
    }
    
    func search(searchText:String) -> Void {
        if searchText.count>0 {
            let results = DataSet.searchDataList(query: searchText)
            dataSetobjects.removeAll()
            for data in results {
                dataSetobjects.append(data)
            }
            dataTable.reloadData()
        }
    }
    
    func initializeData(){
        self.dataSetobjects.removeAll()
        for dataSet in DataSet.getDataSet(dataType: dataType) {
            dataSetobjects.append(dataSet)
        }
    }
    @objc func dismissKeyboard() ->Void {
        //Causes the view (or one of its embedded text fields) to resign the first responder status.
        view.endEditing(true)
    }
    

}
