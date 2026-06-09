package com.vrsalex.taskflow.data.auth

import com.vrsalex.network.public.api.AuthApi
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.auth.SignUpData
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.notify.NotifyRepository
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RegisterRequest

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider,
    private val authObserver: AuthObserver,
    private val notifyRepository: NotifyRepository
): AuthRepository {
    override suspend fun signIn(
        identity: String,
        password: String
    ): Resource<Unit> {
        return authApi.signIn(
            LoginRequest(
                identity = identity,
                password = password
            )
        ).toResource {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
            authObserver.setAuthorized(true)
            notifyRepository.registerDevice()
        }
    }

    override suspend fun signUp(data: SignUpData): Resource<Unit> {
        return authApi.signUp(
            RegisterRequest(
                username = data.username,
                email = data.email,
                fullName = data.fullName,
                password = data.password
            )
        ).toResource {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
            authObserver.setAuthorized(true)
            notifyRepository.registerDevice()
        }
    }


}