//
//  SearchBarView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/13/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import SwiftUI

struct SearchBarView: View {
    var titleText: String
    @Binding var searchText: String
    var action: (String) -> Void
    
    var body: some View {
        HStack {
            TextField(titleText, text: $searchText)
                .padding(7)
                .background(Color(.systemGray6))
                .cornerRadius(8)
                .padding(.horizontal, 15)
                .keyboardType(.numberPad)
            Button("Search") {
                action(titleText)
            }
            .padding(.init(top: 7, leading: 0, bottom: 7, trailing: 7))
            .padding(.horizontal, 15)
        }
    }
}

struct SearchBarView_Preview: PreviewProvider {
    static var previews: some View {
        SearchBarView(titleText: "Zipcode:", searchText: .constant("78759")) { text in
            print(text)
        }
    }
}
