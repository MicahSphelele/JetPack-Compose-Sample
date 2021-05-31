package com.recipeapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class DataStoreManager( private val context: Context) {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "app_settings")
    private var appDataStore: DataStore<Preferences> = context.dataStore

    private suspend fun saveBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)

        appDataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    private suspend fun getBoolean(key: String): Boolean {
        val dataStoreKey = booleanPreferencesKey(key)
        val preferences = appDataStore.data.first()

        return preferences[dataStoreKey] ?: false
    }

}