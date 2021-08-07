//
//  ContentView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/9/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI
import Resolver

struct ContentView: View {
    @State private var selection = 0
    @StateObject var petSearchViewModel = PetSearchViewModel()
    @StateObject var favoritesViewModel =  FavoritePetsViewModel()
    @StateObject var homeViewModel = HomeViewModel()
    @StateObject var settingsViewModel = SettingsViewModel()
    
    var body: some View {
        TabView(){
            HomeView()
                .font(.title)
                .tabItem {
                    VStack {
                        Image(systemName: "house")
                        Text("Home")
                    }
                }
                .tag(0)
                .environmentObject(homeViewModel)
            PetSearchView()
                .font(.title)
                .tabItem {
                    VStack {
                        Image(systemName: "magnifyingglass")
                        Text("search")
                    }
                }
                .tag(1)
                .environmentObject(petSearchViewModel)
            FavoritePetsView()
                .font(.title)
                .tabItem {
                    VStack {
                        Image(systemName: "heart.fill")
                        Text("Favorites")
                    }
                }
                .tag(2)
                .environmentObject(favoritesViewModel)
            SettingsView()
                .font(.title)
                .tabItem {
                    VStack {
                        Image(systemName: "gear")
                        Text("Settings")
                    }
                }
                .tag(3)
                .environmentObject(settingsViewModel)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
