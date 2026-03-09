package vrsalex.app.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds


fun Application.configureRateLimit() {

    install(RateLimit){
        register(RateLimitName("auth_rate")) {
            rateLimiter(limit = 10, refillPeriod = 60.seconds)
            requestKey { call ->
                call.request.origin.remoteHost
            }

            /* В Nginx
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
             */

        }
    }

}