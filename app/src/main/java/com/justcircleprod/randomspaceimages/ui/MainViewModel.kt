package com.justcircleprod.randomspaceimages.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository.DefaultDataStoreRepository
import com.justcircleprod.randomspaceimages.ui.bottomNavigation.BottomNavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(dataStoreRepository: DefaultDataStoreRepository) :
    ViewModel() {
    val themeValue = dataStoreRepository.readSetting(DataStoreConstants.THEME_KEY).asLiveData()

    val startScreen = dataStoreRepository.readSetting(DataStoreConstants.START_SCREEN)

    val bottomNavItems = MutableStateFlow(BottomNavItem.getItems(null))

    // states are stored here so downloads don't overlap
    val savingToGallery = MutableStateFlow(false)
    val sharingImage = MutableStateFlow(false)
}