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
        List {
          searchField

          if viewModel.dataSource.isEmpty {
            emptySection
          } else {
            animalSection
          }
        }
          .listStyle(GroupedListStyle())
          .navigationBarTitle("Dog Search 🐶")
      }
    }
}

private extension PetSearchView {
  var searchField: some View {
    HStack(alignment: .center) {
        TextField("e.g. 78759", text: $viewModel.location)
    }
  }

  var animalSection: some View {
    Section {
      ForEach(viewModel.dataSource, content: AnimalPreviewRow.init(viewModel:))
    }
  }

  var emptySection: some View {
    Section {
      Text("No results")
        .foregroundColor(.gray)
    }
  }
}
