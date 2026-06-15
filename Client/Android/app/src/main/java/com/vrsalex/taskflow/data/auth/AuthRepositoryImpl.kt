package com.vrsalex.taskflow.data.auth

import com.vrsalex.network.public.api.AuthApi
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.auth.AuthTokens
import com.vrsalex.taskflow.domain.auth.LoginData
import com.vrsalex.taskflow.domain.auth.RegisterData
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import vrsalex.shared.api.auth.AuthResponse
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RegisterRequest

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenProvider: TokenProvider,
    private val authObserver: AuthObserver,
) : AuthRepository {

    override suspend fun login(data: LoginData): Resource<AuthTokens> =
        api.signIn(LoginRequest(identity = data.identity, password = data.password))
            .toResource { it.persist() }

    override suspend fun register(data: RegisterData): Resource<AuthTokens> =
        api.signUp(
            RegisterRequest(
                username = data.username,
                email = data.email,
                fullName = data.fullName,
                password = data.password,
            )
        ).toResource { it.persist() }

    private suspend fun AuthResponse.persist(): AuthTokens {
        tokenProvider.saveTokens(accessToken, refreshToken)
        authObserver.setAuthorized(true)
        return AuthTokens(access = accessToken, refresh = refreshToken)
    }
}
