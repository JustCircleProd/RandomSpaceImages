package com.justcircleprod.randomnasaimages.ui.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomnasaimages.ui.baseViewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(roomRepository: DefaultRoomRepository) :
    BaseViewModel(roomRepository) {

    val favourites = MutableLiveData<List<ImageEntry>>()
    val isRefreshing = MutableStateFlow(false)

    fun setFavourites(refresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            isRefreshing.value = refresh

            favourites.postValue(
                roomRepository.getAllFavourites()
            )

            isRefreshing.value = false
        }
    }
}