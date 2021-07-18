//
//  GetAnimalsResponse.swift
//  PetAdoptSampleApp
//
//  Created by laurenyew on 2/14/20.
//  Copyright © 2020 laurenyew. All rights reserved.
//

import Foundation

public struct GetAnimalsResponse: Codable {
    public let animals: [Animal]
    public let pagination: Pagination?
    
    public struct Animal: Codable {
        public let id: Int
        public let organizationId: String?
        public let url: URL?
        public let type: String
        public let species: String
        public let breeds: Breeds?
        public let colors: Colors?
        public let age: String?
        public let gender: String
        public let size: String?
        public let coat: String?
        public let name: String?
        public let description: String?
        public let photos: [Photo]?
        public let status: String?
        public let attributes: Attributes?
        public let environment: Environment?
        public let tags: [String]
        public let contact: Contact
        public let publishDate: String?
        public let distance: Double?
        
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
    
    public struct Breeds: Codable {
        public let primary: String?
        public let secondary: String?
        public let mixed: Bool?
        public let unknown: Bool?
    }
    
    public struct Colors: Codable {
        public  let primary: String?
        public let secondary: String?
        public let tertiary: String?
    }
    
    public struct Photo: Codable {
        public let small: URL?
        public  let medium: URL?
        public let large: URL?
        public let full: URL?
    }
    
    public struct Attributes: Codable {
        public let spayedNeutered: Bool?
        public let houseTrained: Bool?
        public let declawed: Bool?
        public let specialNeeds: Bool?
        public let shotsCurrent: Bool?
        
        enum CodingKeys: String, CodingKey {
            case spayedNeutered = "spayed_neutered"
            case houseTrained = "house_trained"
            case declawed
            case specialNeeds = "special_needs"
            case shotsCurrent = "shots_current"
        }
    }
    
    public struct Environment: Codable {
        public let children: Bool?
        public let dogs: Bool?
        public let cats: Bool?
    }
    
    public struct Contact: Codable {
        public let email: String?
        public let phone: String?
        public let address: Address?
    }
    
    public struct Address: Codable {
        public let address1: String?
        public let address2: String?
        public let city: String?
        public let state: String?
        public let postcode: String?
        public let country: String?
    }
    
    public struct Pagination: Codable {
        public let countPerPage: Int?
        public let totalCount: Int?
        public let currentPage: Int?
        public let totalPages: Int?
        
        enum CodingKeys: String, CodingKey {
            case countPerPage = "count_per_page"
            case totalCount = "total_count"
            case currentPage = "current_page"
            case totalPages = "total_pages"
        }
    }
}
