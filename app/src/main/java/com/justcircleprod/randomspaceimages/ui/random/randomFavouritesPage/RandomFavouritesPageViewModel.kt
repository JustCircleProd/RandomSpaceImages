package com.justcircleprod.randomspaceimages.ui.random.randomFavouritesPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.repository.NASALibraryFavouritesRepositoryImpl
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.random.randomBaseViewModel.RandomBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomFavouritesPageViewModel @Inject constructor(nasaLibraryFavouritesRepository: NASALibraryFavouritesRepositoryImpl) :
    RandomBaseViewModel(nasaLibraryFavouritesRepository) {

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
                nasaLibraryFavouritesRepository.getAllNASALibraryFavourites()
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