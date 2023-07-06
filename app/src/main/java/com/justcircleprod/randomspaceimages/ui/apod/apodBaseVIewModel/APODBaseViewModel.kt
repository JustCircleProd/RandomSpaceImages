package com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreConstants
import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.domain.repository.APODFavouritesRepository
import com.justcircleprod.randomspaceimages.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class APODBaseViewModel(
    protected val apodFavouritesRepository: APODFavouritesRepository,
    protected val settingsRepository: SettingsRepository
) : ViewModel() {
    fun isAddedToFavourites(date: String) = apodFavouritesRepository.isAddedToAPODFavourites(date)
    val qualityOfSavingAndSharingImages =
        settingsRepository.readSetting(DataStoreConstants.QUALITY_OF_SAVING_AND_SHARING_IMAGES)

    fun addToFavourites(apodEntry: APODEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            apodFavouritesRepository.addToAPODFavourites(apodEntry)
        }
    }

    fun removeFromFavourites(apodEntry: APODEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            apodFavouritesRepository.removeFromAPODFavourites(apodEntry)
        }
    }
}