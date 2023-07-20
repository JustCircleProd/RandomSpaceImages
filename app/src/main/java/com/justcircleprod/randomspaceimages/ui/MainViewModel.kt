package com.justcircleprod.randomspaceimages.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreConstants
import com.justcircleprod.randomspaceimages.data.repository.SettingsRepositoryImpl
import com.justcircleprod.randomspaceimages.ui.bottomNavigation.BottomNavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(settingsRepository: SettingsRepositoryImpl) :
    ViewModel() {
    val themeValue = settingsRepository.readSetting(DataStoreConstants.THEME_KEY).asLiveData()

    val startScreen = settingsRepository.readSetting(DataStoreConstants.START_SCREEN)

    val bottomNavItems = MutableStateFlow(BottomNavItem.getItems(null))

    // states are stored here so downloads don't overlap
    val savingToGallery = MutableStateFlow(false)
    val sharingImage = MutableStateFlow(false)

    override fun onCleared() {
        super.onCleared()

        savingToGallery.value = false
        sharingImage.value = false
    }
}