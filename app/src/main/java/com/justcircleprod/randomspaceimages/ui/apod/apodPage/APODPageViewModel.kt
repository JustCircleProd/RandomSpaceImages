package com.justcircleprod.randomspaceimages.ui.apod.apodPage

import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.data.repository.APODFavouritesRepositoryImpl
import com.justcircleprod.randomspaceimages.data.repository.APODRepositoryImpl
import com.justcircleprod.randomspaceimages.data.repository.SettingsRepositoryImpl
import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel.APODBaseViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem.APODStates
import com.justcircleprod.randomspaceimages.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class APODPageViewModel @Inject constructor(
    private val apodRepository: APODRepositoryImpl,
    apodFavouritesRepository: APODFavouritesRepositoryImpl,
    settingsRepository: SettingsRepositoryImpl
) : APODBaseViewModel(apodFavouritesRepository, settingsRepository) {
    val apodList = mutableListOf<APODEntry>()

    val isLoading = MutableStateFlow(true)
    val loadError = MutableStateFlow(false)

    val isRefreshing = MutableStateFlow(false)

    val endReached = MutableStateFlow(false)

    private lateinit var lastDate: Date

    val pickedDateInMills: MutableStateFlow<Long?> = MutableStateFlow(null)

    init {
        loadTodayAPOD()
    }

    fun onEvent(event: APODPageEvent) {
        when (event) {
            is APODPageEvent.LoadTodayAPOD -> {
                loadTodayAPOD(refresh = event.refresh)
            }

            is APODPageEvent.LoadMoreAPODs -> {
                loadMoreAPODs()
            }

            is APODPageEvent.OnDatePicked -> {
                loadAPODByDay(dateInMills = event.dateInMills)
            }

            is APODPageEvent.OnCancelDateButtonClick -> {
                cancelDateAndRefreshList()
            }

            is APODPageEvent.OnFavouriteButtonClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (apodFavouritesRepository.isAddedToAPODFavourites(event.apodEntry.date)) {
                        apodFavouritesRepository.removeFromAPODFavourites(event.apodEntry)
                    } else {
                        apodFavouritesRepository.addToAPODFavourites(event.apodEntry)
                    }
                }
            }

            is APODPageEvent.SaveImage -> {
                saveImage(
                    context = event.context,
                    title = event.title,
                    url = event.url,
                    hdurl = event.hdUrl
                )
            }

            is APODPageEvent.ShareImage -> {
                shareImage(
                    context = event.context,
                    title = event.title,
                    url = event.url,
                    hdurl = event.hdUrl
                )
            }

            is APODPageEvent.ShareVideo -> {
                shareVideo(
                    context = event.context,
                    title = event.title,
                    url = event.url
                )
            }

            is APODPageEvent.Translate -> {
                translate(event.apodIndex)
            }
        }
    }

    private fun loadTodayAPOD(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                clearLists()
            }
            setLoadingOrRefreshing(refresh, true)

            val result = apodRepository.getTodayAPOD()

            if (result is Resource.Success || result.data != null) {
                loadError.value = false

                lastDate = SimpleDateFormat(
                    APODConstants.DATE_FORMAT,
                    Locale.US
                ).parse(result.data!!.date)!!

                apodList.add(result.data)
                apodsStates.value.add(APODStates.fromAPODEntry(result.data))
            } else {
                loadError.value = true
            }

            setLoadingOrRefreshing(refresh, false)
        }
    }

    // load 5 more apods from the lastDate value
    private fun loadMoreAPODs() {
        viewModelScope.launch {
            isLoading.value = true

            val oneDayInMills = 24 * 60 * 60 * 1000
            val minDate = SimpleDateFormat(
                APODConstants.DATE_FORMAT,
                Locale.US
            ).parse(APODConstants.MIN_DATE)!!

            val startDate = Date(lastDate.time)

            while (startDate > minDate && (lastDate minus startDate).time < oneDayInMills * 5) {
                startDate.time -= oneDayInMills
            }

            when {
                startDate == minDate -> {
                    endReached.value = true
                }
                startDate < minDate -> {
                    endReached.value = true
                    return@launch
                }
            }

            val endDate = if (lastDate != minDate) {
                startDate plus ((lastDate minus startDate) minusDays 1)
            } else {
                minDate
            }

            val formattedStartDate = SimpleDateFormat(
                APODConstants.DATE_FORMAT,
                Locale.US
            ).format(startDate)

            val formattedEndDate = SimpleDateFormat(
                APODConstants.DATE_FORMAT,
                Locale.US
            ).format(endDate)


            val result = apodRepository.getAPODsInDateRange(formattedStartDate, formattedEndDate)

            if (result is Resource.Success || result.data != null) {
                loadError.value = false

                val reversedData = result.data!!.reversed()
                lastDate = SimpleDateFormat(
                    APODConstants.DATE_FORMAT,
                    Locale.US
                ).parse(reversedData[reversedData.size - 1].date)!!

                apodList.addAll(reversedData)
                apodsStates.value.addAll(reversedData.map { APODStates.fromAPODEntry(it) })
            } else {
                loadError.value = true
            }

            isLoading.value = false
        }
    }

    private fun loadAPODByDay(dateInMills: Long) {
        viewModelScope.launch {
            pickedDateInMills.value = dateInMills

            clearLists()

            isLoading.value = true

            val result = apodRepository.getAPODByDate(
                SimpleDateFormat(APODConstants.DATE_FORMAT, Locale.US).format(Date(dateInMills))
            )

            if (result is Resource.Success || result.data != null) {
                loadError.value = false

                apodList.add(result.data!!)
                apodsStates.value.add(APODStates.fromAPODEntry(result.data))
            } else {
                loadError.value = true
            }

            isLoading.value = false
        }
    }

    private fun cancelDateAndRefreshList() {
        pickedDateInMills.value = null
        clearLists()
        loadTodayAPOD()
    }

    private fun setLoadingOrRefreshing(refresh: Boolean, targetValue: Boolean) {
        if (refresh) {
            isRefreshing.value = targetValue
        } else {
            isLoading.value = targetValue
        }
    }

    private fun clearLists() {
        apodList.clear()
        apodsStates.value.clear()
        clearLastTexts()
    }

    private infix fun Date.minus(other: Date): Date {
        return Date(this.time - other.time)
    }

    private infix fun Date.plus(other: Date): Date {
        return Date(this.time + other.time)
    }

    private infix fun Date.minusDays(daysCount: Int): Date {
        return Date(this.time - 24 * 60 * 60 * 1000 * daysCount)
    }
}