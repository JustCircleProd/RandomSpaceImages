package com.justcircleprod.randomspaceimages.ui.baseViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.models.ImageEntry
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel(protected val roomRepository: DefaultRoomRepository) : ViewModel() {
    fun isAddedToFavourites(nasaId: String) = roomRepository.isAddedToFavourites(nasaId)

    fun addToFavourites(imageEntry: ImageEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.addToFavourites(imageEntry)
        }
    }

    fun removeFromFavourites(imageEntry: ImageEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.removeFromFavourites(imageEntry)
        }
    }
}