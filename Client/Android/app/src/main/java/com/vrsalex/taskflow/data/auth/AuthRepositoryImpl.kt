package com.vrsalex.taskflow.data.auth

import com.google.firebase.messaging.FirebaseMessaging
import com.vrsalex.network.public.api.AuthApi
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.auth.SignUpData
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.sync.SyncUseCase
import kotlinx.coroutines.suspendCancellableCoroutine
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RegisterRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider,
    private val authObserver: AuthObserver
): AuthRepository {
    override suspend fun signIn(
        identity: String,
        password: String
    ): Resource<Unit> {
        val fcmToken = getFcmToken()
        return authApi.signIn(
            LoginRequest(
                identity = identity,
                password = password,
                fcmToken = fcmToken
            )
        ).toResource {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
            authObserver.setAuthorized(true)
        }
    }

    override suspend fun signUp(data: SignUpData): Resource<Unit> {
        val fcmToken = getFcmToken()
        return authApi.signUp(
            RegisterRequest(
                username = data.username,
                email = data.email,
                fullName = data.fullName,
                password = data.password,
                fcmToken = fcmToken
            )
        ).toResource {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
            authObserver.setAuthorized(true)
        }
    }

    private suspend fun getFcmToken(): String = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token -> continuation.resume(token) }
            .addOnFailureListener { continuation.resume("") }
    }


}