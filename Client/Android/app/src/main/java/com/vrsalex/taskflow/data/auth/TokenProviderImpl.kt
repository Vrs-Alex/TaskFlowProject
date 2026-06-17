package com.vrsalex.taskflow.data.auth

import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.data.local.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow

class TokenProviderImpl(
    private val dataStore: DataStoreManager,
) : TokenProvider {

    override fun getAccessToken(): Flow<String?> = dataStore.getAccessToken()
    override fun getRefreshToken(): Flow<String?> = dataStore.getRefreshToken()

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.setAccessToken(accessToken)
        dataStore.setRefreshToken(refreshToken)
    }
}
