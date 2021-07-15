//
//  SettingsViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/14/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation

class SettingsViewModel: ObservableObject, Identifiable {
    @Published var lastLocation: String = ""
    
    init() {
        lastLocation = getLastSearchTerm() ?? ""
    }
    
    private func getLastSearchTerm() -> String? {
        let userDefaults = UserDefaults.standard
        return userDefaults.value(forKey: "lastLocation") as? String
    }
}
