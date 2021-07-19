//
//  PetDetailsView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/18/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation
import SwiftUI
import PetAdoptCommonViewsFramework

struct PetDetailsView: View {
    private let name: String
    private let gender: String
    private let genderColor: Color
    private let species: String
    private let photoUrl: URL?
    private let age: String
    private let description: String
    private var isFavorite: Bool
    private let favoriteImage: String
    private let updateFavoriteStatus: () -> Void
    
    init(viewModel: AnimalViewModel, updateFavoriteStatus: @escaping () -> Void) {
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
        self.age = viewModel.age
        self.description = viewModel.description
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
        VStack {
            PetAdoptImageView(withURL: self.photoUrl)
                .frame(minWidth: 10, idealWidth: nil, maxWidth: nil, minHeight: 10, idealHeight: 250, maxHeight: 300, alignment: .center)
                .overlay(
                    Button(action: self.updateFavoriteStatus) {
                        Image(systemName: self.favoriteImage)
                            .resizable()
                            .frame(minWidth: 10, idealWidth: 15, maxWidth: 25, minHeight: 10, idealHeight: 15, maxHeight: 20, alignment: .center)
                            .padding(15)
                            .foregroundColor(.red)
                    }, alignment: .bottomTrailing)
            HStack {
                Text("\(self.species) \(self.gender)")
                    .font(.body)
                    .foregroundColor(self.genderColor)
                Text(self.age)
                    .font(.body)
            }
            Text(self.description)
                .font(.body)
                .padding(.top, 7)
            
            Spacer()
        }
        .navigationBarTitle(self.name)
    }
}

struct PetDetailsView_Preview: PreviewProvider {
    static var previews: some View {
        PetDetailsView(
            viewModel: AnimalViewModel(
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
    }
}
