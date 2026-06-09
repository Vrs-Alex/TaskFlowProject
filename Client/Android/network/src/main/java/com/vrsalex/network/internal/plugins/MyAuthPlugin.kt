package com.vrsalex.network.internal.plugins


import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.network.public.provider.TokenProvider
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import vrsalex.shared.api.auth.AuthResponse
import vrsalex.shared.api.auth.RefreshTokenRequest

internal class MyAuthPluginConfig {
    lateinit var tokenProvider: TokenProvider
    lateinit var authObserver: AuthObserver
    lateinit var deviceIdProvider: DeviceIdProvider
}

internal val MyAuthPlugin = createClientPlugin("MyAuthPlugin", ::MyAuthPluginConfig){
    val config = pluginConfig
    val mutex = Mutex()
    val isRefreshRequest = AttributeKey<Boolean>("IsRefreshRequest")

    onRequest { request, _ ->
        if (request.attributes.getOrNull(isRefreshRequest) == true) return@onRequest

        val token = config.tokenProvider.getAccessToken().first()
            ?: return@onRequest
        request.headers["X-Device-Id"] = config.deviceIdProvider.getDeviceId()
        request.headers[HttpHeaders.Authorization] = "Bearer $token"
    }


    client.plugin(HttpSend).intercept { request ->
        try {
            var call = execute(request)
            val isRefreshing = request.attributes.getOrNull(isRefreshRequest) ?: false

            if (call.response.status == HttpStatusCode.Unauthorized && !isRefreshing){
                val newAccessToken = mutex.withLock {
                    val currentAccessToken = config.tokenProvider.getAccessToken().first()
                        ?: return@withLock null.also { config.authObserver.logout() }

                    val tokenInRequest = request.headers[HttpHeaders.Authorization]?.removePrefix("Bearer ")
                    if (currentAccessToken != tokenInRequest) {
                        return@withLock currentAccessToken
                    }

                    val refreshToken = config.tokenProvider.getRefreshToken().first()
                        ?: return@withLock null.also { config.authObserver.logout() }
                    val refreshResponse = client.post("auth/refresh-token") {
                        setAttributes { put(isRefreshRequest, true) }
                        setBody(RefreshTokenRequest(refreshToken))
                    }

                    if (refreshResponse.status == HttpStatusCode.OK){
                        val tokensPair = refreshResponse.body<AuthResponse>()
                        config.tokenProvider.saveTokens(tokensPair.accessToken, tokensPair.refreshToken)
                        tokensPair.accessToken
                    } else {
                        if (refreshResponse.status == HttpStatusCode.Unauthorized){
                            pluginConfig.authObserver.logout()
                        }
                        null
                    }
                }
                if (newAccessToken != null) {
                    val retryRequest = HttpRequestBuilder().takeFrom(request).apply {
                        headers[HttpHeaders.Authorization] = "Bearer $newAccessToken"
                    }
                    call = execute(retryRequest)
                }
            }
            call
        } catch (e: Exception) { throw e }
    }

}