package com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository

import androidx.datastore.preferences.core.stringPreferencesKey
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreManager
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class DefaultDataStoreRepository @Inject constructor(private val dataStoreManager: DataStoreManager) :
    DataStoreRepository {

    override suspend fun saveSetting(key: String, value: String) {
        dataStoreManager.saveSetting(stringPreferencesKey(key), value)
    }

    override fun readSetting(key: String): Flow<String?> =
        dataStoreManager.readSetting(stringPreferencesKey(key))
}