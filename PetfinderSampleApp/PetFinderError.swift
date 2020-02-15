//
//  PetFinderError.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/15/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation

enum PetFinderError: Error {
  case parsing(description: String)
  case network(description: String)
}
