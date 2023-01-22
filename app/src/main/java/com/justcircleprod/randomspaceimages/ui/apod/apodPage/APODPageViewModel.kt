package com.justcircleprod.randomspaceimages.ui.apod.apodPage

import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.data.repositories.apodRepository.DefaultAPODRepository
import com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository.DefaultDataStoreRepository
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel.APODBaseViewModel
import com.justcircleprod.randomspaceimages.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class APODPageViewModel @Inject constructor(
    private val apodRepository: DefaultAPODRepository,
    roomRepository: DefaultRoomRepository,
    dataStoreRepository: DefaultDataStoreRepository
) : APODBaseViewModel(roomRepository, dataStoreRepository) {
    val apodList = MutableStateFlow<MutableList<APODEntry>>(mutableListOf())

    val isLoading = MutableStateFlow(true)
    val loadError = MutableStateFlow(false)

    val isRefreshing = MutableStateFlow(false)

    val endReached = MutableStateFlow(false)

    private lateinit var lastDate: Date

    val pickedDateInMills: MutableStateFlow<Long?> = MutableStateFlow(null)

    init {
        loadTodayAPOD()
    }

    fun loadTodayAPOD(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                apodList.value.clear()
            }
            setLoadingOrRefreshing(refresh, true)

            val result = apodRepository.getTodayAPOD()

            if (result is Resource.Success || result.data != null) {
                loadError.value = false

                lastDate = SimpleDateFormat(
                    APODConstants.DATE_FORMAT,
                    Locale.US
                ).parse(result.data!!.date)!!
                apodList.value =
                    (apodList.value + mutableListOf(result.data)) as MutableList<APODEntry>
            } else {
                loadError.value = true
            }

            setLoadingOrRefreshing(refresh, false)
        }
    }

    // load 5 more apods from the lastDate value
    fun loadMoreAPODs() {
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
                apodList.value = (apodList.value + reversedData) as MutableList<APODEntry>
            } else {
                loadError.value = true
            }

            isLoading.value = false
        }
    }

    fun loadAPODByDay(dateInMills: Long) {
        viewModelScope.launch {
            pickedDateInMills.value = dateInMills

            apodList.value.clear()
            isLoading.value = true

            val result = apodRepository.getAPODByDate(
                SimpleDateFormat(APODConstants.DATE_FORMAT, Locale.US).format(Date(dateInMills))
            )

            if (result is Resource.Success || result.data != null) {
                loadError.value = false

                apodList.value = mutableListOf(result.data!!)
            } else {
                loadError.value = true
            }

            isLoading.value = false
        }
    }

    private fun setLoadingOrRefreshing(refresh: Boolean, targetValue: Boolean) {
        if (refresh) {
            isRefreshing.value = targetValue
        } else {
            isLoading.value = targetValue
        }
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