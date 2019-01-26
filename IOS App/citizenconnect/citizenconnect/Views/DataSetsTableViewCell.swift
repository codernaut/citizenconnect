//
//  DataSetsTableViewCell.swift
//  citizenconnect
//
//  Created by Shahzaib Shahid on 14/03/2018.
//  Copyright © 2018 cfp. All rights reserved.
//

import UIKit

class DataSetsTableViewCell: UITableViewCell {

    @IBOutlet weak var Address: UILabel!
    @IBOutlet weak var Name: UILabel!
    @IBOutlet weak var complainData: UIButton!
    
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
