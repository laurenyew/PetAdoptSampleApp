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
            }
            VStack(alignment: .leading) {
                Text("\(viewModel.name)")
                    .font(.body)
                Text("\(viewModel.gender)")
                    .font(.footnote)
            }
            .padding(.leading, 8)
        }
    }
}
