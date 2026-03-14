package vrsalex.app.plugin

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true // красивый json в dev
            ignoreUnknownKeys = true // не падает на лишние поля в запросе
            encodeDefaults = false // true - всегда включает дефолтные значения, false - пропускает их (может сэкономить трафик)
            explicitNulls = true // false не включает null-значения в json (может сэкономить трафик)
        })
    }
}