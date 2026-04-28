package vrsalex.core.routing

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.routing.Route

/**
 *
 * @param protection Тип защиты [RouteProtection]]
 * @param rateLimitName Какой именно лимитер применить [RateLimitNames]
 * @param build Сам роут
 */
fun Route.protected(
    protection: RouteProtection = RouteProtection.JWT,
    rateLimitName: RateLimitNames? = null,
    build: Route.() -> Unit
) {

    val wrappedWithLimit: Route.() -> Unit = {
        if (rateLimitName != null) {
            rateLimit(RateLimitName(rateLimitName.name)) { build() }
        } else {
            build()
        }
    }

    when (protection) {
        RouteProtection.NONE -> wrappedWithLimit()
        RouteProtection.JWT -> authenticate("auth-jwt") { wrappedWithLimit() }
        RouteProtection.ADMIN -> authenticate("auth-admin") { wrappedWithLimit() }
    }
}


fun ApplicationCall.realIp(): String =
    request.headers["X-Real-IP"]
        ?: request.headers["X-Forwarded-For"]
            ?.split(",")
            ?.firstOrNull()
            ?.trim()
        ?: request.origin.remoteAddress
