<h1 align="center">Random Space Images (APOD)</h1>

<p align="center">  
  🚀 Random Space Images is an app for viewing Random Space and Daily (Astronomy Picture of the Day) content, detailed information about it, add to favourites, save to gallery and search for everything.
</p>

<p align="center">
  <img src="/preview/poster.png"/>
</p>

## Features🔭

Random Space Images combines the content of the two apps, allowing you to view both the Astronomy Picture of the Day and Random Space Images. At the same time, the app does not lose its simplicity and clarity, thanks to a proper separation of content and a light and user-friendliness, but at the same time beautiful and modern interface.

**APOD Screen** | **Random Screen** | **More Screen**
:-: | :-: | :-:
View today's Astronomy Picture of the Day or previous ones in the form of a feed or by a selected date. Detailed view of all information and images. Adding to favorites and saving to the gallery. | Viewing random images from the NASA Library since 1920 (very, very many images). Detailed viewing, adding to favorites and saving to the gallery. | Changing the application theme, choosing the start screen (APOD or Random), viewing information about the application.
<video src='https://user-images.githubusercontent.com/62091531/211642595-e5eef250-047e-4764-aa1f-5ed9c5d954b5.mp4' width="360"/> | <video src='https://user-images.githubusercontent.com/62091531/211642607-9a02c1cf-6ee2-4469-a372-061f0aa363d2.mp4'/> | <video src='https://user-images.githubusercontent.com/62091531/211607890-a313a927-e2b3-4ac7-8148-ec7a1edc7f3c.mp4'/>

## Technologies💻

**Language: Kotlin🏝**  
*Minimum SDK level 21*

Tech stack:
- **Compose**
- Navigation Component
- ViewModel
- Coroutines + Flow
- Room
- DataStore
- Hilt
- Retrofit2, OkHttp3
- Architecture
  - MVVM Architecture
  - Repository Pattern
- Custom Views
  - [Landscapist Glide](https://github.com/skydoves/landscapist#glide), [placeholder](https://github.com/skydoves/landscapist#placeholder)
  - [Jetpack Compose Number Picker](https://github.com/ChargeMap/Compose-NumberPicker)
  - [TouchImageView](https://github.com/MikeOrtiz/TouchImageView)
  
### Tech explanation

The design is 100% based on Compose, Fragments are only used for Navigation, because some problems were found in Compose Navigation with saving states.
Therefore, a hybrid way was chosen, maximizing the benefits of usual and modern solutions.

The principle of "Single Activity" is used. The application is built using MVVM.

API queries are done with Retrofit2, data is converted into Kotlin DataClass with Gson Converter.

Room is used to store favorites.
DataStore is used to store user settings, such as application theme and start screen.
  
## About APIs📲

<img src="https://www.nasa.gov/sites/default/files/thumbnails/image/nasa-logo-web-rgb.png" align="right" width="21%"/>

All media are taken from official APIs from NASA:
- [APOD API](https://github.com/nasa/apod-api) — helps to get Astronomy Picture of the Day.
- [NASA Image and Video Library API](https://images.nasa.gov/docs/images.nasa.gov_api_docs.pdf) — helps to get images from the NASA library
  since 1920.
  
## Copying is prohibited🚫
[<img src="preview/get_it_on_google_play.png" align="left" width="15%"/>](https://play.google.com/store/apps/details?id=com.justcircleprod.randomspaceimages)

**Copying the source code and the application is prohibited.**  
The current working version of the application can be downloaded from the Google Play.
