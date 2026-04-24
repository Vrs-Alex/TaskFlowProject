package com.vrsalex.taskflow.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManagerImpl(
    private val context: Context
): DataStoreManager {

    override fun isFirstLaunch(): Flow<Boolean> = getData(IS_FIRST_LAUNCH).map { it ?: true }
    override suspend fun setFirstLaunch() = saveValue(IS_FIRST_LAUNCH, false)

    override fun getAccessToken(): Flow<String?> = getData(ACCESS_TOKEN)
    override fun getRefreshToken(): Flow<String?> = getData(REFRESH_TOKEN)
    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        saveValue(ACCESS_TOKEN, accessToken)
        saveValue(REFRESH_TOKEN, refreshToken)
    }

    override suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
            preferences[IS_FIRST_LAUNCH] = false
        }
    }

    private fun <T> getData(key: Preferences.Key<T>): Flow<T?> =
        context.dataStore.data.map {
            it[key]
        }

    private suspend fun <T> saveValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    companion object {
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")

        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    }

}