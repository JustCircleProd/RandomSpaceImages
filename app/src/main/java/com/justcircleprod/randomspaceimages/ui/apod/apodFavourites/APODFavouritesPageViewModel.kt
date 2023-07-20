package com.justcircleprod.randomspaceimages.ui.apod.apodFavourites

import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.data.repository.APODFavouritesRepositoryImpl
import com.justcircleprod.randomspaceimages.data.repository.SettingsRepositoryImpl
import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel.APODBaseViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem.APODStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class APODFavouritesPageViewModel @Inject constructor(
    apodFavouritesRepository: APODFavouritesRepositoryImpl,
    settingsRepository: SettingsRepositoryImpl
) : APODBaseViewModel(apodFavouritesRepository, settingsRepository) {

    val favourites = mutableListOf<APODEntry>()

    val isLoading = MutableStateFlow(true)
    val isRefreshing = MutableStateFlow(false)

    init {
        setFavourites()
    }

    fun onEvent(event: APODFavouritesPageEvent) {
        when (event) {
            is APODFavouritesPageEvent.SetFavourites -> {
                setFavourites(event.refresh)
            }

            is APODFavouritesPageEvent.OnFavouriteButtonClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (apodFavouritesRepository.isAddedToAPODFavourites(event.apodEntry.date)) {
                        apodFavouritesRepository.removeFromAPODFavourites(event.apodEntry)
                    } else {
                        apodFavouritesRepository.addToAPODFavourites(event.apodEntry)
                    }
                }
            }

            is APODFavouritesPageEvent.SaveImage -> {
                saveImage(
                    context = event.context,
                    title = event.title,
                    url = event.url,
                    hdurl = event.hdUrl
                )
            }

            is APODFavouritesPageEvent.ShareImage -> {
                shareImage(
                    context = event.context,
                    title = event.title,
                    url = event.url,
                    hdurl = event.hdUrl
                )
            }

            is APODFavouritesPageEvent.ShareVideo -> {
                shareVideo(
                    context = event.context,
                    title = event.title,
                    url = event.url
                )
            }

            is APODFavouritesPageEvent.Translate -> {
                translate(event.apodIndex)
            }
        }
    }

    private fun setFavourites(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                favourites.clear()
                apodsStates.value.clear()
                clearLastTexts()
            }

            setLoadingOrRefreshing(refresh, true)

            val sortedFavourites =
                apodFavouritesRepository.getAllAPODFavourites().sortedByDescending {
                    SimpleDateFormat(APODConstants.DATE_FORMAT, Locale.US).parse(it.date)
                }

            favourites.addAll(sortedFavourites)
            apodsStates.value.addAll(sortedFavourites.map { APODStates.fromAPODEntry(it) })

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