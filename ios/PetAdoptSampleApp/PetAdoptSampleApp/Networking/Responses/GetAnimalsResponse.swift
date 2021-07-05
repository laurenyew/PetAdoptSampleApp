//
//  GetAnimalsResponse.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/14/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation

struct GetAnimalsResponse: Codable {
    let animals: [Animal]
    let pagination: Pagination?
    
    struct Animal: Codable {
        let id: Int
        let organizationId: String?
        let url: URL?
        let type: String
        let species: String
        let breeds: Breeds?
        let colors: Colors?
        let age: String?
        let gender: String
        let size: String?
        let coat: String?
        let name: String?
        let description: String?
        let photos: [Photo]?
        let status: String?
        let attributes: Attributes?
        let environment: Environment?
        let tags: [String]
        let contact: Contact
        let publishDate: String?
        let distance: Double?
        
        enum CodingKeys: String, CodingKey {
            case id
            case organizationId = "organization_id"
            case url
            case type
            case species
            case breeds
            case colors
            case age
            case gender
            case size
            case coat
            case name
            case description
            case photos
            case status
            case attributes
            case environment
            case tags
            case contact
            case publishDate = "published_at"
            case distance
        }
    }
    
    struct Breeds: Codable {
        let primary: String?
        let secondary: String?
        let mixed: Bool?
        let unknown: Bool?
    }
    
    struct Colors: Codable {
        let primary: String?
        let secondary: String?
        let tertiary: String?
    }
    
    struct Photo: Codable {
        let small: URL?
        let medium: URL?
        let large: URL?
        let full: URL?
    }
    
    struct Attributes: Codable {
        let spayedNeutered: Bool?
        let houseTrained: Bool?
        let declawed: Bool?
        let specialNeeds: Bool?
        let shotsCurrent: Bool?
        
        enum CodingKeys: String, CodingKey {
            case spayedNeutered = "spayed_neutered"
            case houseTrained = "house_trained"
            case declawed
            case specialNeeds = "special_needs"
            case shotsCurrent = "shots_current"
        }
    }
    
    struct Environment: Codable {
        let children: Bool?
        let dogs: Bool?
        let cats: Bool?
    }
    
    struct Contact: Codable {
        let email: String?
        let phone: String?
        let address: Address?
    }
    
    struct Address: Codable {
        let address1: String?
        let address2: String?
        let city: String?
        let state: String?
        let postcode: String?
        let country: String?
    }
    
    struct Pagination: Codable {
        let countPerPage: Int?
        let totalCount: Int?
        let currentPage: Int?
        let totalPages: Int?
        
        enum CodingKeys: String, CodingKey {
            case countPerPage = "count_per_page"
            case totalCount = "total_count"
            case currentPage = "current_page"
            case totalPages = "total_pages"
        }
    }
}
