//
//  PetSearchView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/9/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI

struct PetSearchView: View {
    @ObservedObject var searchViewModel: PetSearchViewModel
    
    init(searchViewModel: PetSearchViewModel){
        self.searchViewModel = searchViewModel
    }
    
    var body: some View {
        NavigationView {
            VStack{
                searchField
                AnimalListView(dataSource: searchViewModel.dataSource) { animalRowViewModel in
                    searchViewModel.onFavoriteClicked(animal: animalRowViewModel)
                }
                Spacer()
            }
            .alert(isPresented: $searchViewModel.showError, content: {
                Alert(title: Text("Search Failed"),
                      message: Text(searchViewModel.errorText),
                      dismissButton: .default(Text("OK")))
            })
            .navigationBarTitle("Search Pets")
        }
        
    }
}

private extension PetSearchView {
    var searchField: some View {
        SearchBarView(titleText: "Zipcode:", searchText: $searchViewModel.location) { _ in
            searchViewModel.executeSearch()
        }
    }
}
