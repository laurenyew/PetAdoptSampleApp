//
//  PetAdoptError.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/15/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation

public enum PetAdoptError: Error {
  case parsing(description: String)
  case network(description: String)
}
