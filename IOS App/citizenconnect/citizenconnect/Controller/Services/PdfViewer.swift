//
//  PdfViewer.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 08/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit
import WebKit

class PdfViewer: UIViewController , WKUIDelegate {
    
    @IBOutlet weak var viewPdf: WKWebView!
    var stringPassed:String = ""
    var activityIndicator:UIActivityIndicatorView = UIActivityIndicatorView()
    
    override func loadView() {
        let webConfiguration = WKWebViewConfiguration()
        viewPdf = WKWebView(frame: .zero, configuration: webConfiguration)
        viewPdf.uiDelegate = self
        viewPdf.isOpaque = false;
        viewPdf.backgroundColor =  UIColor.white
        view = viewPdf
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let cancelButton = UIBarButtonItem(image: UIImage(named: "back"), style: .plain, target: self, action: #selector(ImageViewer.dismissController(_:)))
        cancelButton.tintColor = UIColor.white
        self.navigationItem.leftBarButtonItem = cancelButton
        self.navigationItem.title  = stringPassed
        let url : NSURL! = NSURL(string: Services.getFilePath(serviceName: stringPassed))
        viewPdf.load(_: URLRequest(url: url as URL))
        
    }
    
    @objc func dismissController(_ sender: AnyObject?) {
        dismiss(animated: true, completion: nil)
    }
}
