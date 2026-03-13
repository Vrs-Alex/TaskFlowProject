package vrsalex.app.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import vrsalex.core.routing.RateLimitNames
import kotlin.time.Duration.Companion.minutes

/*
        В Nginx
          proxy_set_header Host $host;
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Forwarded-Proto $scheme;
*/

fun Application.configureRateLimit() {

    install(RateLimit){
        global {
            rateLimiter(limit = 150_000_000, refillPeriod = 1.minutes)
        }

        register(RateLimitName(RateLimitNames.LOGIN_AND_REGISTER.name)) {
            rateLimiter(limit = 10_000_000, refillPeriod = 5.minutes)
            requestKey { call ->
                call.request.origin.remoteHost
            }
        }

        register(RateLimitName(RateLimitNames.REFRESH_TOKEN.name)) {
            rateLimiter(limit = 30_000, refillPeriod = 60.minutes)
            requestKey { call ->
                call.request.origin.remoteHost
            }
        }

    }

}


