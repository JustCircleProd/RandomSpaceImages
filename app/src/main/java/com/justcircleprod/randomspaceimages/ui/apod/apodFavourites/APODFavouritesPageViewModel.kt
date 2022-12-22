package com.justcircleprod.randomspaceimages.ui.apod.apodFavourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel.APODBaseViewModel
import com.justcircleprod.randomspaceimages.ui.common.DateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class APODFavouritesPageViewModel @Inject constructor(roomRepository: DefaultRoomRepository) :
    APODBaseViewModel(roomRepository) {

    val favourites = MutableLiveData<List<APODEntry>>()

    val isLoading = MutableStateFlow(true)
    val isRefreshing = MutableStateFlow(false)

    init {
        setFavourites()
    }

    fun setFavourites(refresh: Boolean = false) {
        viewModelScope.launch {
            setLoadingOrRefreshing(refresh, true)

            val sortedFavourites = roomRepository.getAllAPODFavourites().sortedByDescending {
                DateHelper.fromServerFormatToAppFormat(
                    it.date,
                    APODConstants.DATE_FORMAT
                )
            }
            favourites.postValue(
                sortedFavourites
            )

            setLoadingOrRefreshing(refresh, false)
        }
    }

    private fun setLoadingOrRefreshing(refresh: Boolean, targetValue: Boolean) {
        if (refresh) {
            isRefreshing.value = targetValue
        } else {
            isLoading.value = targetValue
        }
    }
}