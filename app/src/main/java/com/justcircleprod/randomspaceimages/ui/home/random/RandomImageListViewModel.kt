package com.justcircleprod.randomspaceimages.ui.home.random

import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.models.ImageEntry
import com.justcircleprod.randomspaceimages.data.remote.RemoteConstants
import com.justcircleprod.randomspaceimages.data.repositories.nasaLibraryRepository.DefaultNASALibraryRepository
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomspaceimages.ui.baseViewModel.BaseViewModel
import com.justcircleprod.randomspaceimages.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RandomImageListViewModel @Inject constructor(
    private val nasaLibraryRepository: DefaultNASALibraryRepository,
    roomRepository: DefaultRoomRepository
) : BaseViewModel(roomRepository = roomRepository) {

    private val page = MutableStateFlow(0)
    private val year = MutableStateFlow(RemoteConstants.NASA_LIBRARY_YEAR_END)

    // contains checked years and corresponding pages count and pages
    private val yearsToPagesCount: MutableMap<Int, Int> = mutableMapOf()
    private val yearsToPages: MutableMap<Int, MutableList<Int>?> = mutableMapOf()

    val images = MutableStateFlow<MutableList<ImageEntry?>>(mutableListOf())

    val isLoading = MutableStateFlow(true)
    val loadError = MutableStateFlow(false)

    val isRefreshing = MutableStateFlow(false)

    val endReached = MutableStateFlow(false)

    init {
        loadImages()
    }

    fun loadImages(refresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            setLoadingOrRefreshing(refresh = refresh, value = true)

            setEndReached()
            if (endReached.value) {
                setLoadingOrRefreshing(refresh = refresh, value = false)
                return@launch
            }

            // if calculations in setYearAndPage went wrong
            // then loadError will be set in that method
            setYearAndPage()

            if (loadError.value) {
                setLoadingOrRefreshing(refresh = refresh, value = false)
                return@launch
            }

            val result = withContext(Dispatchers.IO) {
                nasaLibraryRepository.getImages(
                    yearStart = year.value,
                    yearEnd = year.value,
                    page = page.value
                )
            }

            when {
                result is Resource.Success && result.data!!.collection.items.isNotEmpty() -> {
                    loadError.value = false

                    val newImages = result.data.collection.items.map {
                        ImageEntry(
                            nasaId = it.data.first().nasa_id,
                            title = it.data.first().title,
                            description = it.data.first().description,
                            center = it.data.first().center,
                            location = it.data.first().location,
                            dateCreated = it.data.first().date_created,
                            imageHref = it.links.first().href,
                            secondaryCreator = it.data.first().secondary_creator,
                            photographer = it.data.first().photographer
                        )
                    }

                    if (isRefreshing.value) {
                        images.value = newImages.toMutableList()
                    } else {
                        images.value += newImages
                    }

                    /*addAds()*/
                }
                result is Resource.Error -> {
                    loadError.value = true
                }
            }

            setLoadingOrRefreshing(refresh = refresh, value = false)
        }
    }

    private fun setEndReached() {
        val yearsCount =
            (RemoteConstants.NASA_LIBRARY_YEAR_START..RemoteConstants.NASA_LIBRARY_YEAR_END).toList().size

        // if not all years checked
        if (yearsCount != yearsToPagesCount.size) {
            return
        }

        // if not all pages in years are checked
        yearsToPages.forEach {
            if (it.value != null && it.value!!.size < yearsToPagesCount[it.key]!!) {
                return
            }
        }

        endReached.value = true
    }

    // year is randomized from the set range
    // for this year, a request is made to the api
    // after which the number of pages for this year is determined and randomized pages
    private suspend fun setYearAndPage() {
        // 0 is the initial value, means that page is not set
        page.value = 0

        while (page.value == 0) {
            year.value =
                (RemoteConstants.NASA_LIBRARY_YEAR_START..RemoteConstants.NASA_LIBRARY_YEAR_END)
                    .filter {
                        // exclude those years that we checked
                        if (!yearsToPagesCount.containsKey(it) || !yearsToPages.containsKey(it)) {
                            return@filter true
                        }

                        if (yearsToPages[it] != null && yearsToPages[it]!!.size == yearsToPagesCount[it]!!) {
                            return@filter false
                        }
                        true
                    }.shuffled()[0]

            val pagesCount = getPagesCount(year.value) ?: return

            // if this year has been checked and there are no images for it
            if (pagesCount == 0) {
                continue
            }

            val possiblePages = (1..pagesCount).apply {
                if (yearsToPages.containsKey(year.value)) {
                    filter {
                        // exclude those pages that we checked
                        if (yearsToPages.containsKey(year.value)) {
                            !yearsToPages[year.value]!!.contains(it)
                        } else {
                            true
                        }
                    }
                }
            }

            if (!possiblePages.isEmpty()) {
                page.value = possiblePages.shuffled()[0]
            }

            if (yearsToPages.containsKey(year.value)) {
                yearsToPages[year.value]!!.add(page.value)
            } else {
                yearsToPages[year.value] = mutableListOf(page.value)
            }
        }
    }

    private suspend fun getPagesCount(year: Int): Int? {
        if (yearsToPagesCount.containsKey(year)) {
            return yearsToPagesCount[year]
        }

        // if this year has not been processed yet, then we get data on it
        val result = withContext(Dispatchers.IO) {
            nasaLibraryRepository.getImages(yearStart = year, yearEnd = year, page = 1)
        }

        if (result is Resource.Error) {
            loadError.value = true
            return null
        }

        loadError.value = false

        if (result.data!!.collection.items.isEmpty()) {
            yearsToPagesCount[year] = 0
            yearsToPages[year] = null
            return 0
        }

        val totalHits = result.data.collection.metadata.total_hits

        val pagesCount = if (totalHits >= RemoteConstants.NASA_LIBRARY_MAX_PAGES) {
            val possiblePagesCount = ceil(
                totalHits.toDouble() / RemoteConstants.NASA_LIBRARY_ITEMS_PER_PAGE
            ).toInt()

            // api does not allow getting more than NASA_LIBRARY_MAX_PAGES (100) pages
            if (possiblePagesCount > RemoteConstants.NASA_LIBRARY_MAX_PAGES) {
                RemoteConstants.NASA_LIBRARY_MAX_PAGES
            } else {
                possiblePagesCount
            }
        } else {
            1
        }

        yearsToPagesCount[year] = pagesCount

        return pagesCount
    }

    private fun setLoadingOrRefreshing(refresh: Boolean, value: Boolean) {
        if (refresh) {
            isRefreshing.value = value
        } else {
            isLoading.value = value
        }
    }

    private fun addAds() {
        val itemsCountBetweenAds = 50

        // add null (ad) every itemsCountBetweenAds items
        for (i in 1..images.value.size / itemsCountBetweenAds) {
            if (
                i * itemsCountBetweenAds <= images.value.size - 1 &&
                images.value[i * itemsCountBetweenAds] != null
            ) {
                images.value.add(i * itemsCountBetweenAds, null)
            }
        }
    }
}

