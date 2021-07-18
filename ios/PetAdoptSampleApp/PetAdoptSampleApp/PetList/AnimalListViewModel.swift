//
//  AnimalListViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/17/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation

class AnimalListViewModel: ObservableObject, Identifiable {
    @Published var dataSource: [AnimalRowViewModel] = []
    @Published var showError: Bool = false
    @Published var errorText: String = ""
    
    internal let favoritePetsRepository: FavoritePetsRepository
    
    init(favoritePetsRepository: FavoritePetsRepository) {
        self.favoritePetsRepository = favoritePetsRepository
    }
    
    func onFavoriteClicked(animal: AnimalRowViewModel){
        let isFavorite = !animal.isFavorite
        self.dataSource.first(where: { $0.id == animal.id })?.isFavorite = isFavorite
        
        if isFavorite {
            self.favoritePetsRepository.saveAnimalToFavorites(animal: animal)
        } else {
            self.favoritePetsRepository.removeAnimalFromFavorites(animalId: String(animal.id))
        }
        
        // Tell SwiftUI to update the list
        objectWillChange.send()
    }
}
