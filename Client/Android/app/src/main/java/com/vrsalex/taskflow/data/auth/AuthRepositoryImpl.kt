package com.vrsalex.taskflow.data.auth

import android.location.LocationRequest
import com.vrsalex.network.public.api.auth.AuthApi
import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.auth.SignUpData
import com.vrsalex.taskflow.domain.common.Resource
import com.vrsalex.taskflow.domain.common.toResource
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RegisterRequest

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
): AuthRepository {
    
    
    override suspend fun signIn(
        identity: String,
        password: String
    ): Resource<Unit> =
        authApi.signIn(
            LoginRequest(
                identity = identity,
                password = password
            )
        ).toResource {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
        }


    override suspend fun signUp(data: SignUpData): Resource<Unit> =
        authApi.signUp(
            RegisterRequest(
                username = data.username,
                email = data.email,
                fullName = data.fullName,
                password = data.password,
            )
        ).toResource {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
        }


}