package com.vrsalex.taskflow.data.remote

import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import kotlinx.coroutines.flow.Flow

class TokenProviderImpl(
    private val dataStoreManager: DataStoreManager,
): TokenProvider {

    override fun getAccessToken(): Flow<String?> = dataStoreManager.getAccessToken()

    override fun getRefreshToken(): Flow<String?> = dataStoreManager.getRefreshToken()

    override suspend fun saveTokens(accessToken: String, refreshToken: String) =
        dataStoreManager.saveTokens(accessToken, refreshToken)
}