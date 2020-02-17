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
    
    init(animal: GetAnimalsResponse.Animal) {
        self.animal = animal
    }
}
