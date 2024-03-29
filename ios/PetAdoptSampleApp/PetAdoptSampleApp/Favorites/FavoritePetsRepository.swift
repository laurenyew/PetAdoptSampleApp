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
import Resolver

class FavoritePetsRepository {
    @Injected private var petAdoptYapDatabase: PetAdoptYapDatabase
    
    func saveAnimalToFavorites(animal: Animal){
        let connection = self.petAdoptYapDatabase.database.newConnection()
        connection.readWrite {(transaction) in
            transaction.setObject(animal, forKey: String(animal.id), inCollection: self.petAdoptYapDatabase.collectionName)
        }
    }
    
    func removeAnimalFromFavorites(animalId: String){
        let connection = self.petAdoptYapDatabase.database.newConnection()
        connection.readWrite {(transaction) in
            transaction.removeObject(forKey: animalId, inCollection: self.petAdoptYapDatabase.collectionName)
        }
    }
    
    func getFavorites() -> [Animal]{
        let connection = self.petAdoptYapDatabase.database.newConnection()
        var favorites:[Animal] = []
        connection.read {(transaction) in
            transaction.iterateKeysAndObjects(inCollection: self.petAdoptYapDatabase.collectionName) { (key, animal: Animal, stop) in
                favorites.append(animal)
            }
        }
        return favorites
    }
}
