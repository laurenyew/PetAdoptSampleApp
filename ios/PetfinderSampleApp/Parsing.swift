//
//  Parsing.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/15/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation

import Combine

func decode<T: Decodable>(_ data: Data) -> AnyPublisher<T, PetFinderError> {
  let decoder = JSONDecoder()
  decoder.dateDecodingStrategy = .secondsSince1970

  return Just(data)
    .decode(type: T.self, decoder: decoder)
    .mapError { error in
      .parsing(description: error.localizedDescription)
    }
    .eraseToAnyPublisher()
}
