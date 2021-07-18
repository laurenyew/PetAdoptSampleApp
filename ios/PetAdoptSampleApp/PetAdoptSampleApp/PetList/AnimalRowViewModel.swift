//
//  AnimalRowViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation
import YapDatabase

class AnimalRowViewModel: Identifiable, ObservableObject, Codable {
    let id: Int
    let name: String
    let gender: String
    let species: String
    let photoUrl: URL?
    var isFavorite: Bool
    
    init(id: Int, name: String, gender: String, species: String, photoUrl: URL?, isFavorite: Bool) {
        self.id = id
        self.name = name
        self.gender = gender
        self.species = gender
        self.photoUrl = photoUrl
        self.isFavorite = isFavorite
    }
    
    init(animal: GetAnimalsResponse.Animal, isFavorite: Bool){
        self.id = animal.id
        self.name = animal.name ?? "Unknown"
        self.gender = animal.gender
        self.species = animal.species
        self.photoUrl = animal.photos?.first?.full
        self.isFavorite = isFavorite
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
