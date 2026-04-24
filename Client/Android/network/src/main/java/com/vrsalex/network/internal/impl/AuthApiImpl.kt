package com.vrsalex.network.internal.impl

import com.vrsalex.network.internal.ext.safeCall
import com.vrsalex.network.public.api.AuthApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import vrsalex.shared.api.auth.AuthResponse
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RegisterRequest

internal class AuthApiImpl(
    private val client: HttpClient
): AuthApi {

    override suspend fun signIn(data: LoginRequest): NetworkResult<AuthResponse> =
        safeCall {
            client.post("login"){
                setBody(data)
            }
        }

    override suspend fun signUp(data: RegisterRequest): NetworkResult<AuthResponse> =
        safeCall {
            client.post("register"){
                setBody(data)
            }
        }

    override suspend fun refreshToken(refreshToken: String): NetworkResult<AuthResponse> =
        safeCall {
            client.post("refresh-token"){
                setBody(mapOf("token" to refreshToken))
            }
        }
}