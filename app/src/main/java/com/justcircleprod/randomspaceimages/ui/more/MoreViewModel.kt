package com.justcircleprod.randomspaceimages.ui.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository.DefaultDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(private val dataStoreRepository: DefaultDataStoreRepository) :
    ViewModel() {
    val themeValue = dataStoreRepository.readSetting(DataStoreConstants.THEME_KEY)
    val startScreenValue = dataStoreRepository.readSetting(DataStoreConstants.START_SCREEN)

    fun saveThemeValue(themeValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveSetting(DataStoreConstants.THEME_KEY, themeValue)
        }
    }

    fun saveStartScreenValue(startScreenValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveSetting(DataStoreConstants.START_SCREEN, startScreenValue)
        }
    }
}