package com.justcircleprod.randomspaceimages.data.repository

import androidx.datastore.preferences.core.stringPreferencesKey
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreManager
import com.justcircleprod.randomspaceimages.domain.repository.SettingsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class SettingsRepositoryImpl @Inject constructor(private val dataStoreManager: DataStoreManager) :
    SettingsRepository {

    override suspend fun saveSetting(key: String, value: String) {
        dataStoreManager.saveSetting(stringPreferencesKey(key), value)
    }

    override fun readSetting(key: String): Flow<String?> =
        dataStoreManager.readSetting(stringPreferencesKey(key))
}