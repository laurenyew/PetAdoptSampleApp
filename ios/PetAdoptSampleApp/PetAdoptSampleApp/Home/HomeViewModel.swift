//
//  HomeViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/14/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation

class HomeViewModel: ObservableObject, Identifiable {
    @Published var lastLocation: String = ""
    
    init() {
        lastLocation = getLastSearchTerm() ?? "N/A"
    }
    
    private func getLastSearchTerm() -> String? {
        let userDefaults = UserDefaults.standard
        return userDefaults.value(forKey: "lastLocation") as? String
    }
}
