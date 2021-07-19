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
    
    func saveAnimalToFavorites(animal: AnimalViewModel){
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
    
    func getFavorites() -> [AnimalViewModel]{
        let connection = self.petAdoptYapDatabase.database.newConnection()
        var favorites:[AnimalViewModel] = []
        connection.read {(transaction) in
            transaction.iterateKeysAndObjects(inCollection: self.petAdoptYapDatabase.collectionName) { (key, animal: AnimalViewModel, stop) in
                favorites.append(animal)
            }
        }
        return favorites
    }
}
