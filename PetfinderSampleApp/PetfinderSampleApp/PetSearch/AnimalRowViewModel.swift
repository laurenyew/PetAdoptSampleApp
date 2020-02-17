//
//  AnimalRowViewModel.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation

class AnimalRowViewModel {
    private let animal: GetAnimalsResponse.Animal
    
    var id: String {
        return animal.id
    }
    
    var name: String {
        return animal.name
    }
    
    var photoUrl: URL? {
        return animal.photos.first?.full
    }
    
    init(animal: GetAnimalsResponse.Animal) {
        self.animal = animal
    }
}
