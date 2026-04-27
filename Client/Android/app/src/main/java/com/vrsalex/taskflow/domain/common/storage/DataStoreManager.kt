package com.vrsalex.taskflow.domain.common.storage

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {

    fun getDeviceId(): Flow<String?>
    suspend fun saveDeviceId(deviceId: String)

    fun isFirstLaunch(): Flow<Boolean>
    suspend fun setFirstLaunch()

    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    suspend fun saveTokens(accessToken: String, refreshToken: String)


    suspend fun clearAll()
}