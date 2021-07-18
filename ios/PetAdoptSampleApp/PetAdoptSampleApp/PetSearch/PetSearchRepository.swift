//
//  PetSearchRepository.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/17/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation
import Combine
import PetAdoptNetworkingFramework

/**
 # PetSearchRepository
 
 Business logic for PetSearch
 
 Includes:
 - Backend calls (search pets)
 - Parsing
 - Saving to database (favorites)
 - Saving to userdefaults (last search term)
 
 */
class PetSearchRepository {
    private let searchAPI: PetAdoptSearchAPI
    private let scheduler: DispatchQueue
    private var disposables = Set<AnyCancellable>()
    
    init(searchAPI: PetAdoptSearchAPI = PetAdoptAPI,
         scheduler: DispatchQueue = DispatchQueue(label: "PetSearchRepository")
    ) {
        self.searchAPI = searchAPI
       self.scheduler = scheduler
    }
    
    func searchForNearbyAnimals(forLocation location:String) -> Future <[AnimalRowViewModel], PetAdoptError> {
        return Future() { [weak self] promise in
            guard let self = self else { return }
            self.searchAPI.getAnimals(forLocation: location)
                .map { response in
                    response.animals.map { animal in
                        AnimalRowViewModel(animal: animal, isFavorite: false) //TODO
                    }
                }
                .receive(on: self.scheduler)
                .sink(receiveCompletion: { value in
                    switch value {
                    case .failure(let error):
                        promise(.failure(error))
                    case .finished:
                        break
                    }
                }) { animalViewModels in
                    
                    //TODO: Do some database work on animal view models to mark them as favorites or not...
                    
                    promise(.success(animalViewModels))
                }
                .store(in: &self.disposables)
        }
    }
    
    func saveLastSearchTerm(forLocation location: String){
        let userDefaults = UserDefaults.standard
        userDefaults.setValue(location, forKey: "lastLocation")
    }
    
    func getLastSearchTerm() -> String? {
        let userDefaults = UserDefaults.standard
        return userDefaults.value(forKey: "lastLocation") as? String
    }
}
