package com.justcircleprod.randomspaceimages.ui.random.randomPage

import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryConstants
import com.justcircleprod.randomspaceimages.data.repository.NASALibraryFavouritesRepositoryImpl
import com.justcircleprod.randomspaceimages.data.repository.NASALibraryRepositoryImpl
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.random.randomBaseViewModel.RandomBaseViewModel
import com.justcircleprod.randomspaceimages.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RandomPageViewModel @Inject constructor(
    private val nasaLibraryRepository: NASALibraryRepositoryImpl,
    nasaLibraryFavouritesRepository: NASALibraryFavouritesRepositoryImpl
) : RandomBaseViewModel(nasaLibraryFavouritesRepository = nasaLibraryFavouritesRepository) {

    private val page = MutableStateFlow(0)
    private val year = MutableStateFlow(NASALibraryConstants.YEAR_END)

    // contains checked years and corresponding pages count and pages
    private val yearsToPagesCount: MutableMap<Int, Int> = mutableMapOf()
    private val yearsToPages: MutableMap<Int, MutableList<Int>?> = mutableMapOf()

    val images = MutableStateFlow<MutableList<NASALibraryImageEntry?>>(mutableListOf())

    val isLoading = MutableStateFlow(true)
    val loadError = MutableStateFlow(false)

    val isRefreshing = MutableStateFlow(false)

    val endReached = MutableStateFlow(false)

    init {
        loadImages()
    }

    fun loadImages(refresh: Boolean = false) {
        viewModelScope.launch {
            setLoadingOrRefreshing(refresh = refresh, targetValue = true)

            setEndReached()
            if (endReached.value) {
                setLoadingOrRefreshing(refresh = refresh, targetValue = false)
                return@launch
            }

            // if calculations in setYearAndPage went wrong
            // then loadError will be set in that method
            setYearAndPage()

            if (loadError.value) {
                setLoadingOrRefreshing(refresh = refresh, targetValue = false)
                return@launch
            }

            val result = nasaLibraryRepository.getImages(
                yearStart = year.value,
                yearEnd = year.value,
                page = page.value
            )

            when {
                result is Resource.Success && result.data!!.collection.items.isNotEmpty() -> {
                    loadError.value = false

                    val newImages = result.data.collection.items.map {
                        NASALibraryImageEntry(
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

            setLoadingOrRefreshing(refresh = refresh, targetValue = false)
        }
    }

    private fun setEndReached() {
        val yearsCount =
            (NASALibraryConstants.YEAR_START..NASALibraryConstants.YEAR_END).toList().size

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
                (NASALibraryConstants.YEAR_START..NASALibraryConstants.YEAR_END)
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
        val result =
            nasaLibraryRepository.getImages(yearStart = year, yearEnd = year, page = 1)


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

        val pagesCount = if (totalHits >= NASALibraryConstants.MAX_PAGES) {
            val possiblePagesCount = ceil(
                totalHits.toDouble() / NASALibraryConstants.ITEMS_PER_PAGE
            ).toInt()

            // api does not allow getting more than NASA_LIBRARY_MAX_PAGES (100) pages
            if (possiblePagesCount > NASALibraryConstants.MAX_PAGES) {
                NASALibraryConstants.MAX_PAGES
            } else {
                possiblePagesCount
            }
        } else {
            1
        }

        yearsToPagesCount[year] = pagesCount

        return pagesCount
    }

    private fun setLoadingOrRefreshing(refresh: Boolean, targetValue: Boolean) {
        if (refresh) {
            isRefreshing.value = targetValue
        } else {
            isLoading.value = targetValue
        }
    }
}

