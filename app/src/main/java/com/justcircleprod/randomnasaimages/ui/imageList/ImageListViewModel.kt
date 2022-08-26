package com.justcircleprod.randomnasaimages.ui.imageList

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.data.remote.RemoteConstants
import com.justcircleprod.randomnasaimages.data.repositories.DefaultNASALibraryRepository
import com.justcircleprod.randomnasaimages.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class ImageListViewModel @Inject constructor(private val nasaLibraryRepository: DefaultNASALibraryRepository) :
    ViewModel() {
    private val page = mutableStateOf(0)
    private val year = mutableStateOf(RemoteConstants.NASA_LIBRARY_DATE_END)

    // contains checked years and corresponding pages count and pages
    private val yearsToPagesCount: MutableMap<Int, Int> = mutableMapOf()
    private val yearsToPages: MutableMap<Int, MutableList<Int>?> = mutableMapOf()

    val imageEntries = mutableStateOf<List<ImageEntry>>(listOf())
    val loadError = mutableStateOf("")
    val isLoading = mutableStateOf(true)
    val endReached = mutableStateOf(false)

    fun loadImages() {
        viewModelScope.launch(Dispatchers.Default) {
            isLoading.value = true

            setEndReach()
            if (endReached.value) {
                isLoading.value = false
                return@launch
            }

            // if calculations in setYearAndPage went wrong
            // then loadError will be set in that method
            setYearAndPage()
            if (loadError.value.isNotEmpty()) {
                isLoading.value = false
                return@launch
            }

            val result = withContext(Dispatchers.IO) {
                nasaLibraryRepository.getImages(page.value, year.value, year.value)
            }

            when (result) {
                is Resource.Success -> {
                    imageEntries.value += result.data!!.collection.items.map {
                        ImageEntry(
                            title = it.data.first().title,
                            description = it.data.first().description,
                            creator = it.data.first().secondary_creator,
                            dateCreated = it.data.first().date_created,
                            imageHref = it.links.first().href
                        )
                    }.shuffled()

                    loadError.value = ""
                }
                is Resource.Error -> {
                    loadError.value = result.errorMessage!!
                }
            }

            isLoading.value = false
        }
    }

    private fun setEndReach() {
        val yearsCount =
            (RemoteConstants.NASA_LIBRARY_DATE_START..RemoteConstants.NASA_LIBRARY_DATE_END).toList().size

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
                (RemoteConstants.NASA_LIBRARY_DATE_START..RemoteConstants.NASA_LIBRARY_DATE_END)
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
            nasaLibraryRepository.getImages(1, year, year)
        }

        if (result is Resource.Error) {
            loadError.value = result.errorMessage!!
            return null
        }

        loadError.value = ""

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
}