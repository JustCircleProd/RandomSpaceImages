package com.justcircleprod.randomspaceimages.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class DataStoreManager(context: Context) {
    private val settingsDataStore = context.dataStore

    suspend fun saveSetting(key: Preferences.Key<String>, value: String) {
        settingsDataStore.edit { settings ->
            settings[key] = value
        }
    }

    fun readSetting(key: Preferences.Key<String>) =
        settingsDataStore.data.map { settings ->
            settings[key]
        }
}