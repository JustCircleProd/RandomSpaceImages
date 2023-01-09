<h1 align="center">Random Space Images (APOD)</h1>

<p align="center">  
  🚀 Random Space Images is an app for viewing Random and Daily content (Astronomy Picture of the Day), detailed information about it, add to favourites, save to gallery and search for everything.
</p>
</br>

<p align="center">
  <img src="/images/poster.png"/>
</p>

## About APIs

<img src="https://www.google.com/url?sa=i&url=https%3A%2F%2Fru.m.wikipedia.org%2Fwiki%2F%25D0%25A4%25D0%25B0%25D0%25B9%25D0%25BB%3ANASA_logo.svg&psig=AOvVaw0MxKJiah96_DMJgUoPuyRF&ust=1673360160997000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCNiL68TWuvwCFQAAAAAdAAAAABAE" align="right" width="21%"/>

All media are taken from official APIs from NASA, such
as [APOD API](https://github.com/nasa/apod-api)
and [NASA Image and Video Library API](https://images.nasa.gov/docs/images.nasa.gov_api_docs.pdf).

## Technologies

- Minimum SDK level 21
- Kotlin based
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
    - [Pager layouts](https://google.github.io/accompanist/pager/#pager-layouts)
      , [Indicator](https://google.github.io/accompanist/pager/#indicators)