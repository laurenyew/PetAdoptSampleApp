//
//  AppDelegate+Injection.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/18/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Resolver

extension Resolver: ResolverRegistering {
    public static func registerAllServices() {
        registerDatabases()
        registerRepositories()
    }
    
    private static func registerDatabases(){
        registerPetAdoptYapDatabase()
    }
    
    private static func registerRepositories(){
        register { FavoritePetsRepository() }.scope(.cached)
        register { PetSearchRepository() }.scope(.cached)
    }
}
