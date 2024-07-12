package com.justcircleprod.randomspaceimages.ui.random.search.searchResult

import androidx.lifecycle.SavedStateHandle
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
class SearchResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val nasaLibraryRepository: NASALibraryRepositoryImpl,
    nasaLibraryFavouritesRepository: NASALibraryFavouritesRepositoryImpl
) : RandomBaseViewModel(nasaLibraryFavouritesRepository = nasaLibraryFavouritesRepository) {

    // q comes from api
    // means "Free text search terms to compare to all indexed metadata."
    private var q = savedStateHandle.get<String>(SearchResultFragment.Q_ARGUMENT_NAME) ?: ""
    private var yearStart = savedStateHandle.get<Int>(SearchResultFragment.YEAR_START_ARGUMENT_NAME)
        ?: NASALibraryConstants.YEAR_START
    private var yearEnd = savedStateHandle.get<Int>(SearchResultFragment.YEAR_END_ARGUMENT_NAME)
        ?: NASALibraryConstants.YEAR_END


    private var page = 1
    private var totalPages = 0

    val images = MutableStateFlow<MutableList<NASALibraryImageEntry?>>(mutableListOf())

    val isLoading = MutableStateFlow(false)
    val isRefreshing = MutableStateFlow(false)

    val loadError = MutableStateFlow(false)
    val noResults = MutableStateFlow(false)

    val endReached = MutableStateFlow(false)

    init {
        searchImages()
    }

    // fun to search for a new query
    fun searchImages(refresh: Boolean = false) {
        viewModelScope.launch {
            setLoadingOrRefreshing(refresh = refresh, targetValue = true)
            noResults.value = false
            page = 1

            images.value = mutableListOf()
            setImages()

            setLoadingOrRefreshing(refresh = refresh, targetValue = false)
        }
    }

    // fun to load images by old query on scroll
    fun loadImages() {
        viewModelScope.launch {
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
        val result =
            nasaLibraryRepository.searchImages(q = q, yearStart, yearEnd, page = page)

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

                images.value += newImages

                // addAds()
            }

            is Resource.Error -> {
                loadError.value = true
            }
        }
    }

    private fun setTotalPages(totalHits: Int) {
        val possiblePagesCount = ceil(
            totalHits.toDouble() / NASALibraryConstants.ITEMS_PER_PAGE
        ).toInt()

        // api does not allow getting more than NASA_LIBRARY_MAX_PAGES (100) pages
        totalPages = if (possiblePagesCount > NASALibraryConstants.MAX_PAGES) {
            NASALibraryConstants.MAX_PAGES
        } else {
            possiblePagesCount
        }
    }

    private fun setEndReached() {
        if (page > totalPages) {
            endReached.value = true
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