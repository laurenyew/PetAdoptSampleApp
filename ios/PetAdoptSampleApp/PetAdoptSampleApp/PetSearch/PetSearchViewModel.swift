//
//  PetSearchViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI
import Combine

class PetSearchViewModel: ObservableObject, Identifiable {
    @Published var location: String = ""
    
    @Published var dataSource: [AnimalRowViewModel] = []
    
    @Published var showError: Bool = false
    @Published var errorText: String = ""
    
    private let petSearchRepository: PetSearchRepository
    private let favoritePetsRepository: FavoritePetsRepository
    private var disposables = Set<AnyCancellable>()
    
    init(petSearchRepository: PetSearchRepository, favoritePetsRepository: FavoritePetsRepository) {
        self.petSearchRepository = petSearchRepository
        self.favoritePetsRepository = favoritePetsRepository
        self.location = petSearchRepository.getLastSearchTerm() ?? ""
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
                    self.showError = false
                    self.errorText = ""
                    self.dataSource = result
                })
                .store(in: &disposables)
            
            petSearchRepository.saveLastSearchTerm(forLocation: location)
        }
    }
    
    func onFavoriteClicked(animal: AnimalRowViewModel){
        self.dataSource.first(where: { $0.id == animal.id })?.isFavorite = !animal.isFavorite
        // Tell SwiftUI to update the list
        objectWillChange.send()
    }
}
