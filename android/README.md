# Android PetfinderSampleApp
Sample App created to practice with different Android Tech

<img src="https://github.com/laurenyew/PetfinderSampleApp/blob/master/android/Screenshots/Animal_Search_Android_Demo_Screenshot.png" data-canonical-src="https://github.com/laurenyew/PetfinderSampleApp/blob/master/android/Screenshots/Animal_Search_Android_Demo_Screenshot.png" width="200" height="400" />

## Android Tech
* Kotlin :heavy_check_mark:
* MVVM :heavy_check_mark:
* ViewModels :heavy_check_mark:
* LiveData :heavy_check_mark:
* Room (TODO)
* Coroutines :heavy_check_mark:
* Kotlin Flow (TODO)
* Jetpack Compose :heavy_check_mark:
* Hilt :heavy_check_mark:

## TODO Next
* Fix placeholder image
* Fix images on list
* Fix card look / feel
* Make keyboard hide on search
* Make bottom sheet hide on scroll down and search hide on scroll down

## TODO (Tech)
* Add Kotlin FLow
* Add Room

## TODO (Features)
* Details Screen
* Favorite an animal
* More Filters (Animal type, etc)
* Favorites Screen
* Profile
* Share Animal w/ others
* Dark Mode
* Tablet
* Map? w/ directions
* Onboarding

## Disclaimers

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
These will be used to create your Petfinder API token
