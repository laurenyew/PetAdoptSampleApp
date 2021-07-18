//
//  PetSearchViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI
import Combine
import PetAdoptNetworkingFramework

class PetSearchViewModel: AnimalListViewModel {
    @Published var location: String = ""
    
    private let petSearchRepository: PetSearchRepository
    private var disposables = Set<AnyCancellable>()
    
    init(petSearchRepository: PetSearchRepository, favoritePetsRepository: FavoritePetsRepository) {
        self.petSearchRepository = petSearchRepository
        super.init(favoritePetsRepository: favoritePetsRepository)
        
        self.location = petSearchRepository.getLastSearchTerm() ?? ""
        // Auto execute search for last search term
        if !self.location.isEmpty {
            executeSearch()
        }
    }
    
    func executeSearch(){
        if(location.count < 5){
            errorText = "Invalid Zipcode. Needs at least 5 digits."
        }else{
            petSearchRepository.searchForNearbyAnimals(forLocation: location)
                .eraseToAnyPublisher()
                .receive(on: DispatchQueue.main)
                .sink(receiveCompletion: { [weak self] (value) in
                    guard let self = self else { return }
                    switch(value){
                    case .failure(let petAdoptError):
                        print("Search request failed: \(String(describing: petAdoptError))")
                        self.showError = true
                        switch petAdoptError {
                        case .network(let description):
                            self.errorText = "Network error: \(description)"
                        case .parsing(let description):
                            self.errorText = "Parsing error: \(description)"
                        }
                    case .finished: break
                    }
                }, receiveValue: { [weak self] (result) in
                    guard let self = self else { return }
                    self.handleSearchResult(animals: result)
                })
                .store(in: &disposables)
            
            petSearchRepository.saveLastSearchTerm(forLocation: location)
        }
    }
    
    /**
     Update state for search result
     Also update search results for favorite status
     */
    private func handleSearchResult(animals: [AnimalRowViewModel]) {
        self.showError = false
        self.errorText = ""
        let favoriteIds = favoritePetsRepository.getFavorites().map { animal in
            animal.id
        }
        let result: [AnimalRowViewModel] = animals.map { animal in
            if favoriteIds.contains(animal.id) {
                animal.isFavorite = true
            } else {
                animal.isFavorite = false
            }
            return animal
        }
        self.dataSource = result
    }
}
