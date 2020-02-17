//
//  AnimalRowViewModel.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation

class AnimalRowViewModel: Identifiable {
    private let animal: GetAnimalsResponse.Animal
    
    var id: String {
        return animal.id
    }
    
    var name: String {
        return animal.name
    }
    
    var gender: String {
        return animal.gender
    }
    
    var photoUrl: URL? {
        return animal.photos.first?.full
    }
    
    init(animal: GetAnimalsResponse.Animal) {
        self.animal = animal
    }
}

extension AnimalRowViewModel: Hashable {
    static func == (lhs: AnimalRowViewModel, rhs: AnimalRowViewModel) -> Bool {
      return lhs.id == rhs.id
    }

    func hash(into hasher: inout Hasher) {
      hasher.combine(self.id)
    }
}
