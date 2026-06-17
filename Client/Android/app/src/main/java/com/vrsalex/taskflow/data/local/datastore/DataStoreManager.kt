package com.vrsalex.taskflow.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dataStore")

class DataStoreManager(
    private val context: Context
) {

    suspend fun setAccessToken(token: String) = setData(ACCESS_TOKEN, token)
    fun getAccessToken() = getData(ACCESS_TOKEN)

    suspend fun setRefreshToken(token: String) = setData(REFRESH_TOKEN, token)
    fun getRefreshToken() = getData(REFRESH_TOKEN)

    suspend fun setFirstLaunch(boolean: Boolean) = setData(FIRST_LAUNCH, boolean)
    fun getFirstLaunch() = getData(FIRST_LAUNCH)


    private suspend fun <T> setData(key: Preferences.Key<T>, value: T) = context.dataStore.edit { it[key] = value }

    private fun <T> getData(key: Preferences.Key<T>): Flow<T?> = context.dataStore.data.map { it[key] }
        .catch {
            emit(null)
        }


    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")


    }

}