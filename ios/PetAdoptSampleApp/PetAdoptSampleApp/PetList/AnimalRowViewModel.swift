//
//  AnimalRowViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation
import YapDatabase

class AnimalRowViewModel: Identifiable, ObservableObject {
    private let animal: GetAnimalsResponse.Animal
    
    var id: Int {
        return animal.id
    }
    
    var name: String {
        return animal.name ?? "Unknown"
    }
    
    var gender: String {
        return animal.gender
    }
    
    var photoUrl: URL? {
        return animal.photos?.first?.full
    }
    
    var isFavorite: Bool = false
    
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
