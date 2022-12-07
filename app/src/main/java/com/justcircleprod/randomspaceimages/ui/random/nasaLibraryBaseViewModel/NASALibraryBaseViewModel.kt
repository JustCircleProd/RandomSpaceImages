package com.justcircleprod.randomspaceimages.ui.random.nasaLibraryBaseViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class NASALibraryBaseViewModel(protected val roomRepository: DefaultRoomRepository) :
    ViewModel() {
    fun isAddedToFavourites(nasaId: String) = roomRepository.isAddedToFavourites(nasaId)

    fun addToFavourites(nasaLibraryImageEntry: NASALibraryImageEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.addToFavourites(nasaLibraryImageEntry)
        }
    }

    fun removeFromFavourites(nasaLibraryImageEntry: NASALibraryImageEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.removeFromFavourites(nasaLibraryImageEntry)
        }
    }
}