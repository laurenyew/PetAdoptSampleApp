//
//  PetSearchView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/9/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI
import PetAdoptCommonViewsFramework

struct PetSearchView: View {
    @EnvironmentObject var searchViewModel: PetSearchViewModel
    
    var body: some View {
        NavigationView {
            VStack{
                searchField
                AnimalListView(dataSource: searchViewModel.dataSource) { Animal in
                    searchViewModel.onFavoriteClicked(animal: Animal)
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
        PetAdoptSearchBarView(titleText: "Zipcode:", searchText: $searchViewModel.location) { _ in
            searchViewModel.executeSearch()
        }
    }
}
