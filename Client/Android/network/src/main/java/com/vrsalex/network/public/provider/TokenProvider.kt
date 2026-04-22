package com.vrsalex.network.public.provider

import kotlinx.coroutines.flow.Flow

interface TokenProvider {

    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    suspend fun saveTokens(accessToken: String, refreshToken: String)

}