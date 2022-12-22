package com.justcircleprod.randomspaceimages.ui.random.randomFavouritesPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomspaceimages.ui.random.nasaLibraryBaseViewModel.NASALibraryBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomFavouritesPageViewModel @Inject constructor(roomRepository: DefaultRoomRepository) :
    NASALibraryBaseViewModel(roomRepository) {

    val favourites = MutableLiveData<List<NASALibraryImageEntry>>()

    val isLoading = MutableStateFlow(true)
    val isRefreshing = MutableStateFlow(false)

    init {
        setFavourites()
    }

    fun setFavourites(refresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingOrRefreshing(refresh, true)

            favourites.postValue(
                roomRepository.getAllNASALibraryFavourites()
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