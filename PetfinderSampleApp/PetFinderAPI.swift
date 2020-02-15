//
//  PetFinderApi.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/14/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation
import Combine

protocol PetFinderSearchAPI {
    func getAnimals() -> AnyPublisher {
    
    }
}

struct PetFinderAPI {
    let apiKey = Bundle.main.object(forInfoDictionaryKey: "PetfinderAccessToken") as? String
}
