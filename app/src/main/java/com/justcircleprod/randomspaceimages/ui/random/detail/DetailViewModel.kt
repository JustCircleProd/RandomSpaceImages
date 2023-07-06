package com.justcircleprod.randomspaceimages.ui.random.detail

import com.justcircleprod.randomspaceimages.data.repository.NASALibraryFavouritesRepositoryImpl
import com.justcircleprod.randomspaceimages.ui.random.randomBaseViewModel.RandomBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    nasaLibraryFavouritesRepository: NASALibraryFavouritesRepositoryImpl
) : RandomBaseViewModel(nasaLibraryFavouritesRepository = nasaLibraryFavouritesRepository)