# Android PetAdoptSampleApp
Sample App created to practice with different Android Tech

<img src="./Screenshots/drawer.png" width="200"/> <img src="./Screenshots/home.png" width="200"/> <img src="./Screenshots/search_favorite.png" width="200"/> <img src="./Screenshots/favorites.png" width="200"/> <img src="./Screenshots/all_filters.png" width="200"/> <img src="./Screenshots/organizations.png" width="200"/> <img src="./Screenshots/settings.png" width="200"/>

## Android Tech
* Kotlin :heavy_check_mark:
* MVVM :heavy_check_mark:
* ViewModels :heavy_check_mark:
* Room :heavy_check_mark:
* Coroutines :heavy_check_mark:
* Kotlin Flow :heavy_check_mark:
* Jetpack Compose :heavy_check_mark:
* Hilt :heavy_check_mark:
* ViewBinding :heavy_check_mark:

## TODO Next
* Make keyboard hide on search
* There's a known issue where if you scroll quickly on the list that Picasso will crash the app with "2021-06-23 00:20:27.367 6599-6599/laurenyew.petadoptsampleapp E/AndroidRuntime: FATAL EXCEPTION: main
    Process: laurenyew.petadoptsampleapp, PID: 6599
    kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled; job=StandaloneCoroutine{Cancelled}@bf62df7". Tried to upgrade to Glide / Coil (https://google.github.io/accompanist/glide/) but the libraries are breaking currently with version of Jetpack Compose.

## TODO (Tech)
* Add Jetpack Pages

## TODO (Features)
* Details Screen
* More Search Query Filters (Animal type, etc)
* Profile
* Share Animal w/ others
* Dark Mode
* Tablet
* Map? w/ directions
* Onboarding

## Disclaimers

Powered by [Petfinder](www.petfinder.com)

Using Petfinder API v2 for sample app:
https://www.petfinder.com/developers/v2/docs/

Icons from: https://icons8.com/

## Steps to build the app

To build and use this app yourself with the petfinder API,
Follow the [Getting Authenticated](https://www.petfinder.com/developers/v2/docs/) steps on the Petfinder.com website to create an account and API key with access token. 
Copy your client id / secret into the `local.properties` file as:
``` 
client.id=<your client id>
client.secret=<your client secret>
```
These will be used to create your Petfinder API token. 
If you do not have your token set up properly, you will see a warning on the Home screen and Petfinder's functionality will not work until you've set up the app with the client id and secret.

<img src="./Screenshots/home_warning.png" width="200"/>
