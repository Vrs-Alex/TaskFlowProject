package vrsalex.app.plugin

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ratelimit.*
import vrsalex.core.routing.RateLimitNames
import kotlin.time.Duration.Companion.minutes

fun Application.configureRateLimit() {

    install(RateLimit){
        global {
            rateLimiter(limit = 1_000_000, refillPeriod = 1.minutes)
        }

        register(RateLimitName(RateLimitNames.LOGIN.name)) {
            rateLimiter(limit = 5, refillPeriod = 1.minutes)
            requestKey { call ->
                call.request.origin.remoteHost
            }
        }

        register(RateLimitName(RateLimitNames.REGISTER.name)) {
            rateLimiter(limit = 10, refillPeriod = 5.minutes)
            requestKey { call ->
                call.request.origin.remoteHost
            }
        }

    }

}


