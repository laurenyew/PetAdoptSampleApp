//
//  AnimalPreviewRow.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI

struct AnimalPreviewRow: View {
    private let name: String
    private let gender: String
    private let photoUrl: URL?
    private let isFavorite: Bool
    private let updateFavoriteStatus: () -> Void
    
    init(name: String, gender: String, photoUrl: URL?, isFavorite: Bool, updateFavoriteStatus: @escaping () -> Void) {
        self.name = name
        self.gender = gender
        self.photoUrl = photoUrl
        self.isFavorite = isFavorite
        self.updateFavoriteStatus = updateFavoriteStatus
    }
    
    var body: some View {
        HStack {
            VStack {
                ImageView(withURL: self.photoUrl)
                .aspectRatio(contentMode: .fit)
            }
            VStack(alignment: .leading) {
                Text("\(self.name)")
                    .font(.body)
                Text("\(self.gender)")
                    .font(.footnote)
            }
            .padding(.leading, 8)
        }
        .frame(minWidth: nil, idealWidth: nil, maxWidth: nil, minHeight: nil, idealHeight: nil, maxHeight: 60.0, alignment: Alignment.leading)
    }
}

struct AnimalPreviewRow_Preview: PreviewProvider {
    static var previews: some View {
        AnimalPreviewRow(name: "Happy", gender: "Male", photoUrl: nil, isFavorite: true) { 
            print("favorited")
        }
    }
}
