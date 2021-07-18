//
//  FavoritePetsRepository.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 7/17/21.
//  Copyright © 2021 laurenyew. All rights reserved.
//

import Foundation
import Combine

class FavoritePetsRepository {
    private let scheduler: DispatchQueue
    private var disposables = Set<AnyCancellable>()
    
    init(scheduler: DispatchQueue = DispatchQueue(label: "FavoritePetsRepository")
    ) {
       self.scheduler = scheduler
    }
}
