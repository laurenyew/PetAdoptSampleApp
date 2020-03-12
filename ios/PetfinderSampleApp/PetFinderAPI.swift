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

let petFinderAPI = PetFinder()

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
        
        var request = URLRequest(url: url)
        guard let accessToken = PetFinderAPI.apiKey else {
            let error = PetFinderError.network(description: "Invalid access token")
            return Fail(error: error).eraseToAnyPublisher()
        }
        request.httpMethod = "GET"
        request.addValue("Bearer \(accessToken)", forHTTPHeaderField: "Authorization")
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        return session.dataTaskPublisher(for: request)
            .mapError { error in
                PetFinderError.network(description: error.localizedDescription) // Convert Error to PetFinderError
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
        var queryItems : [URLQueryItem] = []
        if let type = type {
            queryItems.append(URLQueryItem(name: "type", value: type))
        }
        if let breed = breed {
            queryItems.append(URLQueryItem(name: "breed", value: breed))
        }
        if let size = size {
            queryItems.append(URLQueryItem(name: "size", value: size))
        }
        if let gender = gender {
            queryItems.append(URLQueryItem(name: "gender", value: gender))
        }
        if let age = age {
            queryItems.append(URLQueryItem(name: "age", value: age))
        }
        if let color = color {
            queryItems.append(URLQueryItem(name: "color", value: color))
        }
        if let coat = coat {
            queryItems.append(URLQueryItem(name: "coat", value: coat))
        }
        if let status = status {
            queryItems.append(URLQueryItem(name: "status", value: status))
        }
        if let name = name {
            queryItems.append(URLQueryItem(name: "name", value: name))
        }
        if let organization = organization {
            queryItems.append(URLQueryItem(name: "organization", value: organization))
        }
        if let goodWithChildren = goodWithChildren?.description {
            queryItems.append(URLQueryItem(name: "goodWithChildren", value: goodWithChildren))
        }
        if let goodWithDogs = goodWithDogs?.description {
            queryItems.append(URLQueryItem(name: "goodWithDogs", value: goodWithDogs))
        }
        if let location = location {
            queryItems.append(URLQueryItem(name: "location", value: location))
        }
        if let distance = distance?.description {
            queryItems.append(URLQueryItem(name: "distance", value: distance))
        }
        if let page = page?.description {
            queryItems.append(URLQueryItem(name: "page", value: page))
        }
        if let limit = limit?.description {
            queryItems.append(URLQueryItem(name: "limit", value: limit))
        }
        components.queryItems = queryItems

        return components
    }
}
