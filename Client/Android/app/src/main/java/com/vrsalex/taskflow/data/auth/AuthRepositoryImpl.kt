package com.vrsalex.taskflow.data.auth

import android.util.Log
import com.vrsalex.network.public.api.AuthApi
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.auth.DeviceInfo
import com.vrsalex.taskflow.domain.auth.PushPlatform
import com.vrsalex.taskflow.domain.auth.SignUpData
import com.vrsalex.taskflow.domain.auth.toDto
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.notify.PushTokenProvider
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RegisterDeviceRequest
import vrsalex.shared.api.auth.RegisterRequest

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider,
    private val pushTokenProvider: PushTokenProvider,
    private val deviceIdProvider: DeviceIdProvider,
    private val authObserver: AuthObserver
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
            registerDevice()
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
            registerDevice()
        }
    }

    override suspend fun upsertDevice(data: DeviceInfo): Resource<Unit> {
        return authApi.upsertDeviceInfo(
            RegisterDeviceRequest(
                deviceId = data.deviceId,
                platform = data.platform.toDto(),
                token = data.token,
                deviceName = data.deviceName ?: "Android",
            )
        ).toResource { Unit }
    }


    override suspend fun registerDevice(token: String?): Resource<Unit> {
        val deviceId = deviceIdProvider.getDeviceId()
        val token = token ?: pushTokenProvider.getToken()
        Log.i("MYAPP", token)
        if (token.isEmpty()) return Resource.Failure.Error("FCM token not found")
        return upsertDevice(
            DeviceInfo(
                platform = PushPlatform.FCM,
                token = token,
                deviceId = deviceId,
                deviceName = "Mobile $deviceId"
            )
        )
    }

}