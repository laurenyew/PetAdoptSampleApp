//
//  FavoritePetsViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/16/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation

class FavoritePetsViewModel: AnimalListViewModel {    
    func refreshFavorites(){
        let favorites = favoritePetsRepository.getFavorites()
        self.dataSource = favorites
    }
}
