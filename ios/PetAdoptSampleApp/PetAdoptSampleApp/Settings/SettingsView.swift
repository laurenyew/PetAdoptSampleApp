//
//  SettingsView.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/13/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var viewModel: SettingsViewModel
    
    var body: some View {
        NavigationView {
            VStack(alignment: .leading){
                Text("Searches so far:")
                    .padding(7)
                if !viewModel.lastLocation.isEmpty {
                    Text("- \(viewModel.lastLocation)")
                        .padding(7)
                } else {
                    Text("N/A")
                        .padding(7)
                }
                Divider()
                Spacer()
            }.navigationBarTitle("Settings")
            
        }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
            .environmentObject(SettingsViewModel())
    }
}
