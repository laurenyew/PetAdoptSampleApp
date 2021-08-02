//
//  PetDetailsViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/18/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation
import Resolver

class PetDetailsViewModel: ObservableObject, Identifiable {
    @Published var animal: Animal
    @Injected private var favoritePetsRepository: FavoritePetsRepository
    
    init(animal: Animal) {
        self.animal = animal
    }
    
    func onFavoriteClicked(animal: Animal){
        let isFavorite = !animal.isFavorite
        
        if isFavorite {
            self.favoritePetsRepository.saveAnimalToFavorites(animal: animal)
        } else {
            self.favoritePetsRepository.removeAnimalFromFavorites(animalId: String(animal.id))
        }
    }
}
