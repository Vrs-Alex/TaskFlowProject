package vrsalex.app.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import vrsalex.core.routing.RateLimitNames
import kotlin.time.Duration.Companion.minutes

fun Application.configureRateLimit() {

    install(RateLimit){
        global {
            rateLimiter(limit = 150_000_000, refillPeriod = 1.minutes)
        }

        register(RateLimitName(RateLimitNames.LOGIN.name)) {
            rateLimiter(limit = 100, refillPeriod = 1.minutes)
            requestKey { call ->
                call.request.origin.remoteHost
            }
        }

        register(RateLimitName(RateLimitNames.REGISTER.name)) {
            rateLimiter(limit = 100, refillPeriod = 30.minutes)
            requestKey { call ->
                call.request.origin.remoteHost
            }
        }

    }

}


