//
//  FavoritePetsViewModel.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/16/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation

class FavoritePetsViewModel: ObservableObject, Identifiable {
    
    func onFavoriteClicked(animal: AnimalRowViewModel){
        print("Favorite clicked")
    }
}
