package com.justcircleprod.randomnasaimages.ui.baseViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.data.repositories.roomRepository.DefaultRoomRepository
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