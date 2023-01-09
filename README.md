<h1 align="center">Random Space Images (APOD)</h1>

<p align="center">  
  🚀 Random Space Images is an app for viewing Random and Daily content (Astronomy Picture of the Day), detailed information about it, add to favourites, save to gallery and search for everything.
</p>

<p align="center">
  <img src="/preview/poster.png"/>
</p>

## Demo

<p>
    APOD Screen
    <img src="/preview/apod_demo.mp4" align="right" width="360"/>
</p>

<p>
    Random Screen
    <img src="/preview/random_demo.mp4" align="right" width="360"/>
</p>

<p>
    More Screen
    <img src="/preview/more_demo.mp4" align="right" width="360"/>
</p>

## About APIs

<img src="https://www.nasa.gov/sites/default/files/thumbnails/image/nasa-logo-web-rgb.png" align="right" width="21%"/>

All media are taken from official APIs from NASA:

- [APOD API](https://github.com/nasa/apod-api) — helps to get images from the NASA library since
  1920.
- [NASA Image and Video Library API](https://images.nasa.gov/docs/images.nasa.gov_api_docs.pdf) —
  helps to get Astronomy Picture of the Day.

## Technologies

Language: Kotlin Minimum SDK level 21

Tech stack:

- Compose
- Navigation
- ViewModel
- Coroutines + Flow
- Room
    - DataStore
    - Hilt
- Architecture
    - MVVM Architecture (View - ViewModel - Model)
    - Repository Pattern
- Retrofit2, OkHttp3
- Custom Views
    - [Landscapist Glide](https://github.com/skydoves/landscapist#glide)
      , [placeholder](https://github.com/skydoves/landscapist#placeholder)
    - [Jetpack Compose Number Picker](https://github.com/ChargeMap/Compose-NumberPicker)
    - [PhotoView](https://github.com/Baseflow/PhotoView)
    - [Pager](https://google.github.io/accompanist/pager/#pager-layouts)
      , [Pager indicator](https://google.github.io/accompanist/pager/#indicators)