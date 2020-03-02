# Android PetfinderSampleApp
Sample App created to practice with different Android Tech

## Android Tech
* Kotlin
* MVVM
* ViewModels
* LiveData
* Room
* Coroutines
* Kotlin Flow
* Jetpack Compose

## Disclaimers

Using Petfinder API v2 for sample app:
https://www.petfinder.com/developers/v2/docs/

Icons from: https://icons8.com/

## Steps to build the app

To build and use this app yourself with the petfinder API,
Follow the [Getting Authenticated](https://www.petfinder.com/developers/v2/docs/) steps on the Petfinder.com website to create an account and API key with access token. 
Copy the auth token into the source code under `PetFinderApiBuilder` for the constant value of `AUTH_TOKEN`. This will use your auth token when making the network calls.
