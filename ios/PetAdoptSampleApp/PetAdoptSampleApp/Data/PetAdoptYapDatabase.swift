//
//  PetAdoptYapDatabase.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/18/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import YapDatabase
import Resolver

class PetAdoptYapDatabase {
    public let collectionName = "favoritePets"
    public let database: YapDatabase
    
    enum Errors: Error {
        case failedToCreateApplicationSupportDirectory
    }
    
    fileprivate init() {
        let path = (try? PetAdoptYapDatabase.pathForDatabase(named: "PetAdoptDatabase")) ?? "BrokenPetAdoptDatabasePath.sqlite"
        let pathUrl = URL(fileURLWithPath: path)
        self.database = YapDatabase(url: pathUrl)!
        self.database.registerCodableSerialization(AnimalRowViewModel.self, forCollection: collectionName)
    }
    
    private static func pathForDatabase(named name: String) throws -> String {
        let isValidDirectory = try PetAdoptYapDatabase.checkPathDirectoryExistsAndCreateIfDoesNot()
        if isValidDirectory {
            let paths = NSSearchPathForDirectoriesInDomains(.applicationSupportDirectory, .userDomainMask, true)
            let directory: String = paths.first ?? NSTemporaryDirectory()
            let filename: String = {
                return "\(name).sqlite"
            }()
            return (directory as NSString).appendingPathComponent(filename)
        }
        return ""
    }
    
    /**
     Verify that the path (application support) exists
     and if the directory does not exist, create it
     */
    private static func checkPathDirectoryExistsAndCreateIfDoesNot() throws -> Bool {
        let fm = FileManager()
        let paths = NSSearchPathForDirectoriesInDomains(.applicationSupportDirectory, .userDomainMask, true)
        guard let appSupportPath = paths.first else {
            throw Errors.failedToCreateApplicationSupportDirectory
        }
        if !fm.fileExists(atPath: appSupportPath) {
            do {
                try fm.createDirectory(atPath: appSupportPath, withIntermediateDirectories: true, attributes: nil)
            } catch {
                throw Errors.failedToCreateApplicationSupportDirectory
            }
        }
        return true
    }
}

extension Resolver {
    static func registerPetAdoptYapDatabase() {
        register { PetAdoptYapDatabase() }.scope(.application)
    }
}
