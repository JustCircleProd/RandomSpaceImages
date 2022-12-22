package com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class APODBaseViewModel(protected val roomRepository: DefaultRoomRepository) :
    ViewModel() {
    fun isAddedToFavourites(date: String) = roomRepository.isAddedToAPODFavourites(date)

    fun addToFavourites(apodEntry: APODEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.addToAPODFavourites(apodEntry)
        }
    }

    fun removeFromFavourites(apodEntry: APODEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.removeFromAPODFavourites(apodEntry)
        }
    }
}