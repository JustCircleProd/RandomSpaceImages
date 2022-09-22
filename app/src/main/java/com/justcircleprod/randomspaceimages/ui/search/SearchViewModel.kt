package com.justcircleprod.randomspaceimages.ui.search

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
class SearchViewModel @Inject constructor(
    private val nasaLibraryRepository: DefaultNASALibraryRepository,
    roomRepository: DefaultRoomRepository
) : BaseViewModel(roomRepository = roomRepository) {
    // q comes from api
    // means "Free text search terms to compare to all indexed metadata."
    var q = ""
    private var page = 1
    private var totalPages = 0

    val images = MutableStateFlow<MutableList<ImageEntry?>>(mutableListOf())

    val isLoading = MutableStateFlow(false)
    val isRefreshing = MutableStateFlow(false)

    val loadError = MutableStateFlow(false)
    val noResults = MutableStateFlow(false)

    val endReached = MutableStateFlow(false)

    // fun to search for a new query
    fun searchImages(refresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            setLoadingOrRefreshing(refresh = refresh, value = true)
            noResults.value = false
            page = 1

            images.value = mutableListOf()
            setImages()

            setLoadingOrRefreshing(refresh = refresh, value = false)
        }
    }

    // fun to load images by old query on scroll
    fun loadImages() {
        viewModelScope.launch(Dispatchers.Default) {
            isLoading.value = true
            setEndReached()
            page++

            if (endReached.value) {
                isLoading.value = false
                return@launch
            }

            setImages()

            isLoading.value = false
        }
    }

    private suspend fun setImages() {
        val result = withContext(Dispatchers.IO) {
            nasaLibraryRepository.searchImages(q = q, page = page)
        }

        when (result) {
            is Resource.Success -> {
                loadError.value = false

                if (images.value.isEmpty()) {
                    if (result.data!!.collection.items.isEmpty()) {
                        noResults.value = true
                        return
                    }

                    setTotalPages(totalHits = result.data.collection.metadata.total_hits)
                }

                val newImages = result.data!!.collection.items.map {
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

                images.value += newImages

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
            is Resource.Error -> {
                loadError.value = true
            }
        }
    }

    private fun setTotalPages(totalHits: Int) {
        val possiblePagesCount = ceil(
            totalHits.toDouble() / RemoteConstants.NASA_LIBRARY_ITEMS_PER_PAGE
        ).toInt()

        // api does not allow getting more than NASA_LIBRARY_MAX_PAGES (100) pages
        totalPages = if (possiblePagesCount > RemoteConstants.NASA_LIBRARY_MAX_PAGES) {
            RemoteConstants.NASA_LIBRARY_MAX_PAGES
        } else {
            possiblePagesCount
        }
    }

    private fun setEndReached() {
        if (page > totalPages) {
            endReached.value = true
        }
    }

    private fun setLoadingOrRefreshing(refresh: Boolean, value: Boolean) {
        if (refresh) {
            isRefreshing.value = value
        } else {
            isLoading.value = value
        }
    }
}