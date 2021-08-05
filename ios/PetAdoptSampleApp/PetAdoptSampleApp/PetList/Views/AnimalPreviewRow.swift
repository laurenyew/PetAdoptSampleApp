//
//  AnimalPreviewRow.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/17/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import SwiftUI
import PetAdoptCommonViewsFramework

struct AnimalPreviewRow: View {
    private let name: String
    private let gender: String
    private let genderColor: Color
    private let species: String
    private let photoUrl: URL?
    private var isFavorite: Bool
    private let favoriteImage: String
    private let updateFavoriteStatus: () -> Void
    
    init(viewModel: Animal, updateFavoriteStatus: @escaping () -> Void) {
        self.name = viewModel.name
        self.gender = viewModel.gender
        switch viewModel.gender {
        case "Male":
            self.genderColor = .blue
        case "Female":
            self.genderColor = .pink
        default:
            self.genderColor = .black
        }
        switch viewModel.species {
        case "Dog":
            self.species = "🐶"
        case "Cat":
            self.species = "🐱"
        default:
            self.species = viewModel.species
        }
        self.photoUrl = viewModel.photoUrl
        self.isFavorite = viewModel.isFavorite
        if isFavorite {
            favoriteImage = "heart.fill"
        } else {
            favoriteImage = "heart"
        }
        self.updateFavoriteStatus = updateFavoriteStatus
    }
    
    var body: some View {
        HStack {
            PetAdoptImageView(withURL: self.photoUrl)
                .frame(minWidth: 10, idealWidth: 50, maxWidth: 60, minHeight: 10, idealHeight: 50, maxHeight: 60, alignment: .center)
            VStack(alignment: .leading) {
                Text("\(self.name)")
                    .font(.body)
                Text("\(self.gender) \(self.species)")
                    .font(.footnote)
                    .foregroundColor(self.genderColor)
            }
            .padding(.leading, 8)
            Spacer()
            Button(action: self.updateFavoriteStatus) {
                Image(systemName: self.favoriteImage)
                    .resizable()
                    .frame(minWidth: 10, idealWidth: 15, maxWidth: 25, minHeight: 10, idealHeight: 15, maxHeight: 20, alignment: .center)
                    .padding(15)
                    .foregroundColor(.red)
            }
            
        }
        .frame(minWidth: nil, idealWidth: nil, maxWidth: nil, minHeight: nil, idealHeight: nil, maxHeight: nil, alignment: Alignment.leading)
    }
}

struct AnimalPreviewRow_Preview: PreviewProvider {
    static var previews: some View {
        Group{
            AnimalPreviewRow(
                viewModel: Animal(
                    id: 1,
                    name: "Happy",
                    gender: "Male",
                    species: "Dog",
                    age: "2 years",
                    description: "A wonderful pet\nLooking for a good home\nLoves to play ball and fetch and eat and play and sleep.",
                    photoUrl: nil,
                    isFavorite: true)) {
                print("favorited")
            }
            AnimalPreviewRow(
                viewModel: Animal(
                    id: 2,
                    name: "Cute",
                    gender: "Female",
                    species: "Cat",
                    age: "1 years",
                    description: "A sweet kitty",
                    photoUrl: nil,
                    isFavorite: false)) {
                print("favorited")
            }
        }
        .previewLayout(.fixed(width: 300, height: 70))
    }
}
