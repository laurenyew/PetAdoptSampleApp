//
//  PetFinderApi.swift
//  PetfinderSampleApp
//
//  Created by laurenyew on 2/14/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation
import Combine

protocol PetFinderSearchAPI {
    func getDogsNearMe(forLocation location: String) -> AnyPublisher<GetAnimalsResponse, PetFinderError>
    
    func getAnimals(
        forType type: String?,
        breed: String?,
        size: String?,
        gender: String?,
        age: String?,
        color: String?,
        coat: String?,
        status: String?,
        name: String?,
        organization: String?,
        goodWithChildren: Bool?,
        goodWithDogs: Bool?,
        goodWithCats: Bool?,
        location: String?,
        distance: Int?,
        page: Int?,
        limit: Int?) -> AnyPublisher<GetAnimalsResponse, PetFinderError>
}

class PetFinder {
    private let session: URLSession
    
    init(session: URLSession = .shared) {
        self.session = session
    }
}

// MARK: - PetFinderSearchAPI
extension PetFinder: PetFinderSearchAPI {
    func getDogsNearMe(forLocation location: String) -> AnyPublisher<GetAnimalsResponse, PetFinderError> {
        return getAnimals(forType: "Dog", location: location, distance: 50)
    }
    
    func getAnimals(forType type: String? = nil, breed: String? = nil, size: String? = nil, gender: String? = nil, age: String? = nil, color: String? = nil, coat: String? = nil, status: String? = nil, name: String? = nil, organization: String? = nil, goodWithChildren: Bool? = nil, goodWithDogs: Bool? = nil, goodWithCats: Bool? = nil, location: String? = nil, distance: Int? = nil, page: Int? = nil, limit: Int? = nil) -> AnyPublisher<GetAnimalsResponse, PetFinderError> {
        let components = makeGetAnimalsComponents(type: type, breed: breed, size: size, gender: gender, age: age, color: color, coat: coat, status: status, name: name, organization: organization, goodWithChildren: goodWithChildren, goodWithDogs: goodWithDogs, goodWithCats: goodWithCats, location: location, distance: distance, page: page, limit: limit)
        return searchPets(withComponents: components)
    }
    
    private func searchPets<T>(
        withComponents components: URLComponents
    ) -> AnyPublisher<T, PetFinderError> where T: Decodable {
        guard let url = components.url else {
            let error = PetFinderError.network(description: "Couldn't create URL")
            return Fail(error: error).eraseToAnyPublisher()
        }
        
        let request = NSMutableURLRequest(url: url)
        guard let accessToken = PetFinderAPI.apiKey else {
            let error = PetFinderError.network(description: "Invalid access token")
            return Fail(error: error).eraseToAnyPublisher()
        }
        
        request.setValue("Bearer \(accessToken)", forHTTPHeaderField: "Authorization")
        
        return session.dataTaskPublisher(for: URLRequest(url: url))
            .mapError { error in
                .network(description: error.localizedDescription) // Convert Error to PetFinderError
        }
        .flatMap(maxPublishers: .max(1)) { pair in // Get first value result
            decode(pair.data)
        }
        .eraseToAnyPublisher() // Remove the AnyPublisher type
    }
}

// MARK: - PetFinder API
extension PetFinder {
    struct PetFinderAPI {
        static let scheme = "https"
        static let host = "api.petfinder.com"
        static let path = "/v2"
        static let apiKey = Bundle.main.object(forInfoDictionaryKey: "PetfinderAccessToken") as? String
    }
    
    func makeGetAnimalsComponents(
        type: String? = nil,
        breed: String? = nil,
        size: String? = nil,
        gender: String? = nil,
        age: String? = nil,
        color: String? = nil,
        coat: String? = nil,
        status: String? = nil,
        name: String? = nil,
        organization: String? = nil,
        goodWithChildren: Bool? = nil,
        goodWithDogs: Bool? = nil,
        goodWithCats: Bool? = nil,
        location: String? = nil,
        distance: Int? = nil,
        page: Int? = nil,
        limit: Int? = nil) -> URLComponents {
        
        var components = URLComponents()
        components.scheme = PetFinderAPI.scheme
        components.host = PetFinderAPI.host
        components.path = PetFinderAPI.path + "/animals"
        components.queryItems = [
            URLQueryItem(name: "type", value: type),
            URLQueryItem(name: "breed", value: breed),
            URLQueryItem(name: "size", value: size),
            URLQueryItem(name: "gender", value: gender),
            URLQueryItem(name: "age", value: age),
            URLQueryItem(name: "color", value: color),
            URLQueryItem(name: "coat", value: coat),
            URLQueryItem(name: "status", value: status),
            URLQueryItem(name: "name", value: name),
            URLQueryItem(name: "organization", value: organization),
            URLQueryItem(name: "goodWithChildren", value: goodWithChildren?.description),
            URLQueryItem(name: "goodWithDogs", value: goodWithDogs?.description),
            URLQueryItem(name: "goodWithCats", value: goodWithCats?.description),
            URLQueryItem(name: "location", value: location),
            URLQueryItem(name: "distance", value: distance?.description),
            URLQueryItem(name: "page", value: page?.description),
            URLQueryItem(name: "limit", value: limit?.description)
        ]

        return components
    }
}
