package com.justcircleprod.randomspaceimages.ui.apod.apodPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.remote.apod.responses.APODItem
import com.justcircleprod.randomspaceimages.data.repositories.apodRepository.DefaultAPODRepository
import com.justcircleprod.randomspaceimages.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class APODPageViewModel @Inject constructor(
    private val apodRepository: DefaultAPODRepository
) : ViewModel() {
    val apodList = MutableStateFlow<MutableList<APODItem>>(mutableListOf())

    val isLoading = MutableStateFlow(true)
    val loadError = MutableStateFlow(false)

    private var today: String = ""

    init {
        loadTodayAPOD()
    }

    private fun loadTodayAPOD() {
        viewModelScope.launch {
            isLoading.value = true

            val result = apodRepository.getTodayAPOD()

            if (result is Resource.Success || result.data != null) {
                loadError.value = false

                today = result.data!!.date
                apodList.value.add(result.data)
            } else {
                loadError.value = true
            }

            isLoading.value = false
        }
    }
}