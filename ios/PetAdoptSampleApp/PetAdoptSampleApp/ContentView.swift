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
 
    let petSearchViewModel = PetSearchViewModel(
        petSearchRepository: PetSearchRepository(),
        favoritePetsRepository: FavoritePetsRepository()
    )
    let favoritesViewModel = FavoritePetsViewModel()
    
    let homeViewModel = HomeViewModel()
    
    let settingsViewModel = SettingsViewModel()
    
    var body: some View {
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
            FavoritePetsView()
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
