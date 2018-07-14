//
//  DataSetLsitViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 14/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import MapKit
import GoogleMaps
class POIItem: NSObject, GMUClusterItem {
    var position: CLLocationCoordinate2D
    var name: String!
    var address: String!
    
    init(position: CLLocationCoordinate2D, name: String,address: String) {
        self.position = position
        self.name = name
        self.address = address
    }
}

class DataSetLsitViewController: UIViewController,UITableViewDataSource ,UITableViewDelegate,UISearchBarDelegate , CLLocationManagerDelegate ,UIScrollViewDelegate
    ,GMUClusterManagerDelegate,
    GMSMapViewDelegate
{
    
    
    @IBOutlet weak var dataTable: UITableView!
    
    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var DataSetListView: UITableView!
    
    private let locationManager = CLLocationManager()
    @IBOutlet weak var googleMap: GMSMapView!
    private var clusterManager: GMUClusterManager!
    
    var dataType:String = ""
    var togleViewBtn:UIButton?
    var dataSetobjects = [DataSet]()
    var mSegue:UIStoryboardSegue!
    
    var mapView:UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mapView = UIBarButtonItem(image: UIImage(named: "location_white"), style: .plain, target: self, action: #selector(switchToMapView))
        mapView.tintColor = UIColor.white
        self.navigationItem.rightBarButtonItem = mapView
        
        let camera = GMSCameraPosition.camera(withLatitude: -33.86, longitude: 151.20, zoom: 6.0)
        
        googleMap.isMyLocationEnabled = true
        //Location Manager code to fetch current location
        self.locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        self.locationManager.startUpdatingLocation()

        // Setup Cluster manager
        let iconGenerator = GMUDefaultClusterIconGenerator()
        let algorithm = GMUNonHierarchicalDistanceBasedAlgorithm()
        let renderer = GMUDefaultClusterRenderer(mapView: googleMap,
                                                 clusterIconGenerator: iconGenerator)
        clusterManager = GMUClusterManager(map: googleMap, algorithm: algorithm,
                                           renderer: renderer)
        clusterManager.setDelegate(self, mapDelegate: self)
        
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        view.addGestureRecognizer(tap)
        let cancelButton = UIBarButtonItem(image: UIImage(named: "back"), style: .plain, target: self, action: #selector(ImageViewer.dismissController(_:)))
        cancelButton.tintColor = UIColor.white
        self.navigationItem.leftBarButtonItem = cancelButton
        let cancelButtonAttributes = [NSAttributedStringKey.foregroundColor: UIColor(hexString: "#26a69a")]
        UIBarButtonItem.appearance().setTitleTextAttributes(cancelButtonAttributes , for: .normal)
        self.navigationItem.backBarButtonItem?.title = ""
        self.navigationItem.title = dataType
        togleViewBtn = UIButton(frame: CGRect(origin: CGPoint(x: self.view.frame.width - 70, y: self.view.frame.height-100), size: CGSize(width: 50, height: 50)))
        togleViewBtn?.setImage(#imageLiteral(resourceName: "ic_list_view"), for: UIControlState.normal)
        togleViewBtn?.isHidden = true
        self.navigationController?.view.addSubview(togleViewBtn!)
        togleViewBtn?.addTarget(self, action: #selector(switchToListView), for: .touchUpInside)
        initializeData()
        
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        self.mSegue = segue
    }

    
    func clusterManager(clusterManager: GMUClusterManager, didTapCluster cluster: GMUCluster) {
        let newCamera = GMSCameraPosition.camera(withTarget: cluster.position,
                                                           zoom: googleMap.camera.zoom + 1)
        let update = GMSCameraUpdate.setCamera(newCamera)
        googleMap.moveCamera(update)
    }
    
    func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        
        if marker.userData is POIItem {
            let item:POIItem = marker.userData as! POIItem;
            marker.title = item.name;
            marker.snippet = item.address
            mapView.selectedMarker = marker
            return true
        }
        return true
    }
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        let location = locations.last
            //Stop updating location otherwise it will come again and again in this delegate
        self.locationManager.stopUpdatingLocation()
        let camera = GMSCameraPosition.camera(withLatitude: (location?.coordinate.latitude)!, longitude: (location?.coordinate.longitude)!, zoom: 15.0)
        self.googleMap?.animate(to: camera)
        LoadMarker(moveCamPosition: false)
    }
    func LoadMarker(moveCamPosition:Bool){
        for DataSet in dataSetobjects
        {
            let item = POIItem(position: CLLocationCoordinate2DMake(DataSet.latitude, DataSet.longitude), name: DataSet.name,address: DataSet.address)
            clusterManager.add(item)
        }
        if moveCamPosition && dataSetobjects.count>0 {
            let camera = GMSCameraPosition.camera(withLatitude: dataSetobjects[0].latitude, longitude: dataSetobjects[0].longitude, zoom: 12.0)
            self.googleMap?.animate(to: camera)
        }
        
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

        var image = UIImage(named: "complain")?.withRenderingMode(.alwaysTemplate)
        image = image?.maskWithColor(color: UIColor(hexString: Colors.darkGrey))
        cell.complainData.setImage(image, for: .normal)
        cell.complainData.addTarget(forControlEvents: .touchUpInside) { (button) -> Void in
            let popupVC = self.mSegue.destination as! PopUpDataFeedbackViewController
            popupVC.type = self.dataType
            popupVC.locationName = self.dataSetobjects[indexPath.row].name
            popupVC.locationAddress = self.dataSetobjects[indexPath.row].address
            self.performSegue(withIdentifier: "popUpComplainData", sender: self)
        }
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
            googleMap.clear()
            clusterManager.clearItems()
            LoadMarker(moveCamPosition: true)
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
    @objc func switchToMapView(){
        googleMap.isHidden = false
        togleViewBtn?.isHidden = false
        dataTable.isHidden = true
        
        self.navigationItem.rightBarButtonItem = nil
    }
    @objc func switchToListView(){
        dataTable.isHidden = false
        googleMap.isHidden = true
        togleViewBtn?.isHidden = true
        self.navigationItem.rightBarButtonItem = mapView
    }
}

