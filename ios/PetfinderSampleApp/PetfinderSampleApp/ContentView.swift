//
//  ContentView.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/9/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI

struct ContentView: View {
    @State private var selection = 0
 
    let petSearchViewModel = PetSearchViewModel(petFinderSearchAPI: petFinderAPI)
    
    var body: some View {
        TabView(){
            PetSearchView(viewModel: self.petSearchViewModel)
                .font(.title)
                .tabItem {
                    VStack {
                        Image("circle")
                        Text("Home")
                    }
                }
                .tag(0)
            FavoritePetsView()
                .font(.title)
                .tabItem {
                    VStack {
                        Image("square")
                        Text("Favorites")
                    }
                }
                .tag(1)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
