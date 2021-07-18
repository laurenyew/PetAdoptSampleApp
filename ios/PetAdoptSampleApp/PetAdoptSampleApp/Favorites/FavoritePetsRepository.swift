//
//  FavoritePetsRepository.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/17/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation
import Combine
import YapDatabase

class FavoritePetsRepository {
    private let databaseCollectionName = "favoritePets"
    private let database: YapDatabase
    
    init() {
        // Create and/or Open the database file & Configure database
        self.database = YapDatabase()
        self.database.registerCodableSerialization(AnimalRowViewModel.self, forCollection: databaseCollectionName)
    }
    
    func saveAnimalToFavorites(animal: AnimalRowViewModel){
        let connection = self.database.newConnection()
        connection.readWrite {(transaction) in
            transaction.setObject(animal, forKey: String(animal.id), inCollection: databaseCollectionName)
        }
    }
    
    func removeAnimalFromFavorites(animalId: String){
        let connection = self.database.newConnection()
        connection.readWrite {(transaction) in
            transaction.removeObject(forKey: animalId, inCollection: databaseCollectionName)
        }
    }
    
    func getFavorites() -> [AnimalRowViewModel]{
        let connection = self.database.newConnection()
        var favorites:[AnimalRowViewModel] = []
        connection.read {(transaction) in
            transaction.iterateKeysAndObjects(inCollection: databaseCollectionName) { (key, animal: AnimalRowViewModel, stop) in
                favorites.append(animal)
            }
        }
        return favorites
    }
}
