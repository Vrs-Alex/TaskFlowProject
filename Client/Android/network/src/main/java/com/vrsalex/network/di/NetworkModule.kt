package com.vrsalex.network.di

import com.vrsalex.network.internal.plugins.MyAuthPlugin
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.TokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val networkModule = module {

    single<String>(named("baseUrl")){
        "http://192.168.0.189:8080/api/v1/"
    }

    single<Json>(named("json")) {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = false
        }
    }

    single<HttpClient> {
        val tokenProvider = get<TokenProvider>()
        val authObserver = get<AuthObserver>()

        HttpClient(OkHttp){
            defaultRequest {
                url(get<String>(named("baseUrl")))
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }

            install(ContentNegotiation){
                json(
                    get<Json>(named("json"))
                )
            }

            install(HttpTimeout){
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 15_000
                requestTimeoutMillis = 20_000
            }

            install(HttpRequestRetry){
                maxRetries = 3
                exponentialDelay(base = 2.0, maxDelayMs = 10_000)
                retryOnExceptionIf { _, cause ->
                    cause is IOException
                }
                retryIf { request, response ->
                    !response.status.isSuccess()
                            && response.status.value >= 500
                            && request.method == HttpMethod.Get
                }
            }

            install(WebSockets){
                contentConverter = KotlinxWebsocketSerializationConverter(get<Json>(named("json")))
                pingInterval = 15.seconds
            }

            install(MyAuthPlugin){
                this.tokenProvider = tokenProvider
                this.authObserver = authObserver
            }

            install(Logging){
                level = LogLevel.BODY
                logger = Logger.ANDROID
            }

        }
    }

    includes(apiModule)
}