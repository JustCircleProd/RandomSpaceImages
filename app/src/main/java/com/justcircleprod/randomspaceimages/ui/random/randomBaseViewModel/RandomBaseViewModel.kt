package com.justcircleprod.randomspaceimages.ui.random.randomBaseViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.domain.repository.NASALibraryFavouritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class RandomBaseViewModel(protected val nasaLibraryFavouritesRepository: NASALibraryFavouritesRepository) :
    ViewModel() {
    fun isAddedToFavourites(nasaId: String) =
        nasaLibraryFavouritesRepository.isAddedToNASALibraryFavouritesLiveData(nasaId)

    fun addToFavourites(nasaLibraryImageEntry: NASALibraryImageEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            nasaLibraryFavouritesRepository.addToNASALibraryFavourites(nasaLibraryImageEntry)
        }
    }

    fun removeFromFavourites(nasaLibraryImageEntry: NASALibraryImageEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            nasaLibraryFavouritesRepository.removeFromNASALibraryFavourites(nasaLibraryImageEntry)
        }
    }
}