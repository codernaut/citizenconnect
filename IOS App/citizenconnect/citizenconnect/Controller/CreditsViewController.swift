//
//  CreditsViewController.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 22/06/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit

class CreditsViewController: UIViewController {

    @IBOutlet weak var talhaProfile: UIButton! //http://linkedin.com/in/talhaahmadkhanjang
    @IBOutlet weak var gulzaibProfile: UIButton! //https://www.linkedin.com/in/codernaut/
    @IBOutlet weak var ayeshaAProfile: UIButton! //https://www.linkedin.com/in/ayesha-akhtar-336033119/
    @IBOutlet weak var ayeshaMProfile: UIButton!// https://www.linkedin.com/in/ayesha-mehmood-2b1bb4146
    @IBOutlet weak var wahibProfile: UIButton! //https://www.linkedin.com/in/wahibhaq/
    @IBOutlet weak var yusraProfile: UIButton! // https://www.linkedin.com/in/yusra-dar-b4378661
    @IBOutlet weak var kiranProfile: UIButton!//https://www.linkedin.com/in/kiran-majeed-89b75a39
    @IBOutlet weak var hamidProfile: UIButton! //https://www.linkedin.com/in/engineeredvirus/
    @IBOutlet weak var asimProfile: UIButton! //http://pk.linkedin.com/in/aghaffar
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = "Credits"
        // Do any additional setup after loading the view.
        asimProfile.addTarget(self, action: #selector(asim), for: .touchUpInside)
        hamidProfile.addTarget(self, action: #selector(hamid), for: .touchUpInside)
        kiranProfile.addTarget(self, action: #selector(kiran), for: .touchUpInside)
        yusraProfile.addTarget(self, action: #selector(yusra), for: .touchUpInside)
        wahibProfile.addTarget(self, action: #selector(wahib), for: .touchUpInside)
        ayeshaMProfile.addTarget(self, action: #selector(ayeshaM), for: .touchUpInside)
        ayeshaAProfile.addTarget(self, action: #selector(ayeshaA), for: .touchUpInside)
        gulzaibProfile.addTarget(self, action: #selector(gulzaib), for: .touchUpInside)
        talhaProfile.addTarget(self, action: #selector(talha), for: .touchUpInside)
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
    @objc func asim () {
        if let url = URL(string: "http://pk.linkedin.com/in/aghaffar") {
            UIApplication.shared.open(url, options: [:])
        }
    }
    @objc func hamid () {
        if let url = URL(string: "https://www.linkedin.com/in/engineeredvirus/") {
            UIApplication.shared.open(url, options: [:])
        }
    }
    @objc func kiran () {
        if let url = URL(string: "https://www.linkedin.com/in/kiran-majeed-89b75a39") {
            UIApplication.shared.open(url, options: [:])
        }
    }
    @objc func yusra () {
        if let url = URL(string: "https://www.linkedin.com/in/yusra-dar-b4378661") {
            UIApplication.shared.open(url, options: [:])
        }
    }
    @objc func wahib () {
        if let url = URL(string: "https://www.linkedin.com/in/wahibhaq/") {
            UIApplication.shared.open(url, options: [:])
        }
    }
    @objc func ayeshaM () {
        if let url = URL(string: "https://www.linkedin.com/in/ayesha-mehmood-2b1bb4146") {
            UIApplication.shared.open(url, options: [:])
        }
    }
    @objc func ayeshaA () {
        if let url = URL(string: "https://www.linkedin.com/in/ayesha-akhtar-336033119/") {
            UIApplication.shared.open(url, options: [:])
        }
    }
    @objc func gulzaib () {
        if let url = URL(string: "https://www.linkedin.com/in/codernaut/") {
            UIApplication.shared.open(url, options: [:])
        }
    }
    @objc func talha () {
        if let url = URL(string: "http://linkedin.com/in/talhaahmadkhanjang") {
            UIApplication.shared.open(url, options: [:])
        }
    }

}
