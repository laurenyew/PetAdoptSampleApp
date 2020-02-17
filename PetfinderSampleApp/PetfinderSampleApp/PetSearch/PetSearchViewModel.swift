//
//  PetSearchViewModel.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation
import Combine

class PetSearchViewModel: ObservableObject, Identifiable {
    @Published var location: String = ""
    
    @Published var dataSource: [AnimalRowViewModel] = []
    
    private let petFinderSearchAPI: PetFinderSearchAPI
    
    private var disposables = Set<AnyCancellable>()
    
    init(petFinderSearchAPI: PetFinderSearchAPI) {
        self.petFinderSearchAPI = petFinderSearchAPI
    }
}
