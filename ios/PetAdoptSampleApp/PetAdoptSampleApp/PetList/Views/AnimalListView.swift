//
//  AnimalListView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/16/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import SwiftUI

struct AnimalListView: View {
    let dataSource: [AnimalRowViewModel]
    let onFavoriteClicked: (AnimalRowViewModel) -> Void
    
    init(dataSource: [AnimalRowViewModel],
         onFavoriteClicked: @escaping (AnimalRowViewModel) -> Void
    ){
        self.dataSource = dataSource
        self.onFavoriteClicked = onFavoriteClicked
    }
    
    var body: some View {
        if(dataSource.isEmpty){
            emptySection
        } else {
            animalSection
        }
    }
    
    var animalSection: some View {
        List{
            ForEach(dataSource, content: AnimalPreviewRow.init(viewModel:))
        }.listStyle(PlainListStyle())
    }
    
    var emptySection: some View {
        Text("No results")
            .foregroundColor(.gray)
    }
}
