//
//  SearchBarView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/13/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import SwiftUI

/// SearchBar generic w/ title & search text + button
public struct PetAdoptSearchBarView: View {
    public let titleText: String
    @Binding public var searchText: String
    public let action: (String) -> Void
    
    public init(titleText: String, searchText: Binding<String>, action: @escaping (String) -> Void) {
        self.titleText = titleText
        self._searchText = searchText
        self.action = action
    }

    public var body: some View {
        HStack {
            TextField(titleText, text: $searchText)
                .padding(7)
                .background(Color(.systemGray6))
                .cornerRadius(8)
                .padding(.horizontal, 10)
                .overlay(Button("Search") {
                    action(titleText)
                    self.hideKeyboard()
                }.padding(7)
                .padding(.horizontal, 10), alignment: .trailing)
                .keyboardType(.numberPad)
        }
    }
}

struct PetAdoptSearchBarView_Preview: PreviewProvider {
    static var previews: some View {
        PetAdoptSearchBarView(titleText: "Zipcode:", searchText: .constant("")) { text in
            print(text)
        }
    }
}

#if canImport(UIKit)
extension View {
    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}
#endif
