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
                if(viewModel.dataSource.isEmpty){
                    emptySection
                } else {
                    animalSection
                }
                Spacer()
            }
            .navigationBarTitle("Search Pets")
        }
        
    }
}

private extension PetSearchView {
    var searchField: some View {
        HStack(alignment: .center) {
            TextField("Zipcode:", text: $viewModel.location)
                .padding(7)
                .background(Color(.systemGray6))
                .cornerRadius(8)
                .padding(.horizontal, 15)
                .keyboardType(.numberPad)
        }
    }
    
    var animalSection: some View {
        List{
            ForEach(viewModel.dataSource, content: AnimalPreviewRow.init(viewModel:))
        }
    }
    
    var emptySection: some View {
        Text("No results")
            .foregroundColor(.gray)
    }
}
