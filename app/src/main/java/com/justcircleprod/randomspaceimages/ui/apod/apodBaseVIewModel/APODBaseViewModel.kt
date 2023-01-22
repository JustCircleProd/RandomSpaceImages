package com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository.DefaultDataStoreRepository
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class APODBaseViewModel(
    protected val roomRepository: DefaultRoomRepository,
    protected val dataStoreRepository: DefaultDataStoreRepository
) :
    ViewModel() {
    fun isAddedToFavourites(date: String) = roomRepository.isAddedToAPODFavourites(date)
    val qualityOfSavingAndSharingImages =
        dataStoreRepository.readSetting(DataStoreConstants.QUALITY_OF_SAVING_AND_SHARING_IMAGES)

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