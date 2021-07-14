//
//  SearchBarView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/13/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import SwiftUI

/// SearchBar generic w/ title & search text + button
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
                .padding(.horizontal, 10)
                .overlay(Button("Search") {
                    action(titleText)
                }.padding(7)
                .padding(.horizontal, 10), alignment: .trailing)
                .keyboardType(.numberPad)
        }
    }
}

struct SearchBarView_Preview: PreviewProvider {
    static var previews: some View {
        SearchBarView(titleText: "Zipcode:", searchText: .constant("")) { text in
            print(text)
        }
    }
}
