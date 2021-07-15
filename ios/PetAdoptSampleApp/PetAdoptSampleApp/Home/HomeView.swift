//
//  HomeView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/13/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import SwiftUI

struct HomeView: View {
    @ObservedObject var viewModel: HomeViewModel
    
    init(viewModel: HomeViewModel){
        self.viewModel = viewModel
    }
    
    var body: some View {
        VStack(alignment: .center) {
            Image("Icon")
                .resizable()
                .frame(width: 50.0, height: 50.0)
                .scaledToFit()
            Divider()
            VStack(alignment: .leading) {
                Text("Your last search zipcode: \(viewModel.lastLocation)")
                    .padding(7)
                Divider()
                Text("Welcome to the sample PetAdopt App!")
                    .padding(7)
                Text("This sample uses Petfinder APIs to demo SwiftUI, Combine, and other iOS tech / architecture.")
                    .padding(7)
                Text("Feel free to browse the bottom tab bar to see all the features!" )
                    .padding(7)
            }
            Spacer()
        }
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView(viewModel: HomeViewModel())
    }
}
