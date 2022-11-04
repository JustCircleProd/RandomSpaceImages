package com.justcircleprod.randomspaceimages.ui

import androidx.lifecycle.ViewModel
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository.DefaultDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(dataStoreRepository: DefaultDataStoreRepository) :
    ViewModel() {
    val themeValue = dataStoreRepository.readSetting(DataStoreConstants.THEME_KEY)
}