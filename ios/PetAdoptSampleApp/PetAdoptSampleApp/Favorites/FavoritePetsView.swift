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
    @EnvironmentObject var favoritesViewModel: FavoritePetsViewModel
    
    var body: some View {
        NavigationView {
            VStack{
                AnimalListView(dataSource: favoritesViewModel.dataSource) { Animal in
                    favoritesViewModel.onFavoriteClicked(animal: Animal)
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
        FavoritePetsView()
            .environmentObject(FavoritePetsViewModel())
    }
}
