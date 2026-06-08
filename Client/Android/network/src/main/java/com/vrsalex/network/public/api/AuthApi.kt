package com.vrsalex.network.public.api

import com.vrsalex.network.public.common.NetworkResult
import vrsalex.shared.api.auth.AuthResponse
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RegisterDeviceRequest
import vrsalex.shared.api.auth.RegisterRequest

interface AuthApi {

    suspend fun signIn(data: LoginRequest): NetworkResult<AuthResponse>

    suspend fun signUp(data: RegisterRequest): NetworkResult<AuthResponse>

    suspend fun refreshToken(refreshToken: String): NetworkResult<AuthResponse>

    suspend fun upsertDeviceInfo(data: RegisterDeviceRequest): NetworkResult<Unit>


}