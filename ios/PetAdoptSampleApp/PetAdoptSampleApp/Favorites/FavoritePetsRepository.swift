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
        //        let connection: YapDatabase.YapDatabase = db.newConnection()
        //        connection.readWrite {(transaction) in
        //            transaction.setObject(animal, forKey: animal.animalId, inCollection: databaseCollectionName)
        //        }
    }
    
    func getFavorites() -> [AnimalRowViewModel]{
        return []
    }
    
    // Get a connection to the database
    // (You can have multiple connections for concurrency.)
    
    
    //        // Write an item to the database
    //        connection.readWrite {(transaction) in
    //          transaction.setObject("hello", forKey: "world", inCollection: "test")
    //        }
    //
    //        // Read it back
    //        connection.read {(transaction) in
    //          let str = transaction.object(forKey: "world", inCollection: "test") as? String
    //          // str == "hello"
    //        }
}
