//
//  AnimalPreviewRow.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI

struct AnimalPreviewRow: View {
    private let viewModel: AnimalRowViewModel
    
    init(viewModel: AnimalRowViewModel) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        HStack {
            VStack {
                ImageView(withURL: viewModel.photoUrl)
                .aspectRatio(contentMode: .fit)
            }
            VStack(alignment: .leading) {
                Text("\(viewModel.name)")
                    .font(.body)
                Text("\(viewModel.gender)")
                    .font(.footnote)
            }
            .padding(.leading, 8)
        }
        .frame(minWidth: nil, idealWidth: nil, maxWidth: nil, minHeight: nil, idealHeight: nil, maxHeight: 60.0, alignment: Alignment.leading)
    }
}

struct AnimalPreviewRow_Preview: PreviewProvider {
    static var previews: some View {
        let animal = GetAnimalsResponse.Animal(id: 1, organizationId: "testOrg", url: nil, type: "dog", species: "dog", breeds: nil, colors: nil, age: "2", gender: "female", size: "large", coat: "shiny", name: "Lucy", description: "A very nice dog", photos: nil, status: "adoptable", attributes: nil, environment: nil, tags: [], contact: GetAnimalsResponse.Contact(email: nil, phone: nil, address: nil), publishDate: nil, distance: 50.0)
        AnimalPreviewRow(viewModel: AnimalRowViewModel(animal:animal))
    }
}
