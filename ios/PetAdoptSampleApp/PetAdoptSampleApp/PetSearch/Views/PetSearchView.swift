//
//  PetSearchView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/9/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI

struct PetSearchView: View {
    @ObservedObject var viewModel: PetSearchViewModel
    
    init(viewModel: PetSearchViewModel){
        self.viewModel = viewModel
    }
    
    var body: some View {
        NavigationView {
            VStack{
                searchField
                AnimalListView(dataSource: viewModel.dataSource)
                Spacer()
            }
            .alert(isPresented: $viewModel.showError, content: {
                Alert(title: Text("Search Failed"),
                      message: Text(viewModel.errorText),
                      dismissButton: .default(Text("OK")))
            })
            .navigationBarTitle("Search Pets")
        }
        
    }
}

private extension PetSearchView {
    var searchField: some View {
        SearchBarView(titleText: "Zipcode:", searchText: $viewModel.location) { _ in
            viewModel.executeSearch()
        }
    }
}
