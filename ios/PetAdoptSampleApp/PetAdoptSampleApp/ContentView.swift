//
//  ContentView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/9/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI

struct ContentView: View {
    @State private var selection = 0
    
    var body: some View {
        let petSearchRepository = PetSearchRepository()
        let favoritePetsRepository = FavoritePetsRepository()
        
        let petSearchViewModel = PetSearchViewModel(
            petSearchRepository: petSearchRepository,
            favoritePetsRepository: favoritePetsRepository
        )
        let favoritesViewModel = FavoritePetsViewModel(
            favoritePetsRepository: favoritePetsRepository
        )
        
        let homeViewModel = HomeViewModel()
        
        let settingsViewModel = SettingsViewModel()
        
        TabView(){
            HomeView(viewModel: homeViewModel)
                .font(.title)
                .tabItem {
                    VStack {
                        Image(systemName: "house")
                        Text("Home")
                    }
                }
                .tag(0)
            PetSearchView(
                searchViewModel: petSearchViewModel
            )
            .font(.title)
            .tabItem {
                VStack {
                    Image(systemName: "magnifyingglass")
                    Text("search")
                }
            }
            .tag(1)
            FavoritePetsView(favoritesViewModel: favoritesViewModel)
                .font(.title)
                .tabItem {
                    VStack {
                        Image(systemName: "heart.fill")
                        Text("Favorites")
                    }
                }
                .tag(2)
            SettingsView(viewModel: settingsViewModel)
                .font(.title)
                .tabItem {
                    VStack {
                        Image(systemName: "gear")
                        Text("Settings")
                    }
                }
                .tag(3)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
