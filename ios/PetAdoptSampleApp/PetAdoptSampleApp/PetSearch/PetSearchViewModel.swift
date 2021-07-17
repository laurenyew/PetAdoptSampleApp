//
//  PetSearchViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI
import Combine

class PetSearchViewModel: ObservableObject, Identifiable {
    @Published var location: String = ""
    
    @Published var dataSource: [AnimalRowViewModel] = []
    
    @Published var showError: Bool = false
    @Published var errorText: String = ""
    
    private let PetAdoptSearchAPI: PetAdoptSearchAPI
    
    private var disposables = Set<AnyCancellable>()
    
    init(PetAdoptSearchAPI: PetAdoptSearchAPI,
         scheduler: DispatchQueue = DispatchQueue(label: "PetSearchViewModel")) {
        self.PetAdoptSearchAPI = PetAdoptSearchAPI
        location = getLastSearchTerm() ?? ""
//        $location
//            .dropFirst(1)
//            .debounce(for: .seconds(0.5), scheduler: scheduler)
//            .sink(receiveValue: searchForNearbyDogs(forLocation:))
//            .store(in: &disposables)
    }
    
    func executeSearch(){
        if(location.count < 5){
            errorText = "Invalid Zipcode. Needs at least 5 digits."
        }else{
            searchForNearbyDogs(forLocation: location)
            saveLastSearchTerm(forLocation: location)
        }
    }
    
    private func searchForNearbyDogs(forLocation location:String) {
        PetAdoptSearchAPI.getDogsNearMe(forLocation: location)
            .map { response in
                response.animals.map(AnimalRowViewModel.init)
            }
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { [weak self] value in
                guard let self = self else { return }
                switch value {
                case .failure(let error):
                    self.errorText = error.localizedDescription
                    self.showError = true
                    self.dataSource = [] // Clear out data on failure
                case .finished:
                    break
                }
            }) { [weak self] animalViewModels in
                guard let self = self else { return }
                self.errorText = ""
                self.showError = false
               
                //TODO: Do some database work on animal view models to mark them as favorites or not...
                
                self.dataSource = animalViewModels// Success: Update data source
            }
            .store(in: &disposables)
    }
    
    private func saveLastSearchTerm(forLocation location: String){
        let userDefaults = UserDefaults.standard
        userDefaults.setValue(location, forKey: "lastLocation")
    }
    
    private func getLastSearchTerm() -> String? {
        let userDefaults = UserDefaults.standard
        return userDefaults.value(forKey: "lastLocation") as? String
    }
}
