package com.justcircleprod.randomspaceimages.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository.DefaultDataStoreRepository
import com.justcircleprod.randomspaceimages.ui.theme.ThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(dataStoreRepository: DefaultDataStoreRepository) :
    ViewModel() {
    val themeValue = dataStoreRepository.readSetting(DataStoreConstants.THEME_KEY).asLiveData()
    var themeState = ThemeState.NOT_APPLIED
}