package com.justcircleprod.randomspaceimages.ui.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreConstants
import com.justcircleprod.randomspaceimages.data.repository.SettingsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(private val settingsRepository: SettingsRepositoryImpl) :
    ViewModel() {
    val themeValue = settingsRepository.readSetting(DataStoreConstants.THEME_KEY)
    val startScreenValue = settingsRepository.readSetting(DataStoreConstants.START_SCREEN)
    val qualityOfSavingAndSharingImages =
        settingsRepository.readSetting(DataStoreConstants.QUALITY_OF_SAVING_AND_SHARING_IMAGES)

    fun saveThemeValue(themeValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveSetting(DataStoreConstants.THEME_KEY, themeValue)
        }
    }

    fun saveStartScreenValue(startScreenValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveSetting(DataStoreConstants.START_SCREEN, startScreenValue)
        }
    }

    fun saveQualityOfSavingAndSharingImagesValue(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveSetting(
                DataStoreConstants.QUALITY_OF_SAVING_AND_SHARING_IMAGES,
                value
            )
        }
    }
}