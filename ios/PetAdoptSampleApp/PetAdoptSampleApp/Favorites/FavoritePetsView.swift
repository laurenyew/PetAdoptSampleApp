//
//  FavoritePetsView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/9/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI
import Resolver

struct FavoritePetsView: View {
    @ObservedObject var favoritesViewModel: FavoritePetsViewModel
    
    init(favoritesViewModel: FavoritePetsViewModel){
        self.favoritesViewModel = favoritesViewModel
    }
    
    var body: some View {
        NavigationView {
            VStack{
                AnimalListView(dataSource: favoritesViewModel.dataSource) { AnimalViewModel in
                    favoritesViewModel.onFavoriteClicked(animal: AnimalViewModel)
                }
                Spacer()
            }
            .onAppear(perform: {
                favoritesViewModel.refreshFavorites()
            })
            .alert(isPresented: $favoritesViewModel.showError, content: {
                Alert(title: Text("Get Favorites Failed"),
                      message: Text(favoritesViewModel.errorText),
                      dismissButton: .default(Text("OK")))
            })
            .navigationBarTitle("Favorite Pets")
        }
    }
}

struct FavoritePetsView_Previews: PreviewProvider {
    static var previews: some View {
        FavoritePetsView(favoritesViewModel: FavoritePetsViewModel())
    }
}
