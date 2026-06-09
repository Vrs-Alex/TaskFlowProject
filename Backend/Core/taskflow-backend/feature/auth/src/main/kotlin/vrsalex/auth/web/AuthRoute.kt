package vrsalex.auth.web

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import vrsalex.auth.domain.service.AuthService
import vrsalex.core.routing.*
import vrsalex.shared.api.auth.AuthResponse
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RefreshTokenRequest
import vrsalex.shared.api.auth.RegisterRequest

class AuthRoute(private val service: AuthService) : AppRoute {

    override fun Route.registerRoutes() {

        protected(
            protection = RouteProtection.NONE,
            rateLimitName = RateLimitNames.LOGIN
        ) {
            post("/auth/login") {
                val request = call.receive<LoginRequest>()
                val ip = call.realIp()
                val userAgent = call.request.headers["User-Agent"] ?: "Unknown"

                val tokens = service.login(request.toUser().copy(ipAddress = ip, agent = userAgent))
                call.respond(HttpStatusCode.OK, AuthResponse(tokens.accessToken, tokens.refreshToken))
            }
        }


        protected(
            protection = RouteProtection.NONE,
            rateLimitName = RateLimitNames.REGISTER
        ) {
            post("/auth/register") {
                val request = call.receive<RegisterRequest>()
                val ip = call.realIp()
                val userAgent = call.request.headers["User-Agent"] ?: "Unknown"
                val tokens = service.register(request.toUserCreate(), ip, userAgent)
                call.respond(HttpStatusCode.Created, AuthResponse(tokens.accessToken, tokens.refreshToken))
            }
        }



        post("/auth/refresh-token") {
            val request = call.receive<RefreshTokenRequest>()

            val ip = call.realIp()
            val userAgent = call.request.headers["User-Agent"] ?: "Unknown"
            val tokens = service.refreshToken(request.token, ip, userAgent)
            call.respond(AuthResponse(tokens.accessToken, tokens.refreshToken))
        }

        post("/auth/logout") {
            val refreshToken = call.request.headers["Authorization"]?.removePrefix("Bearer ") ?: ""
            service.logout(refreshToken, call.request.origin.remoteHost)
            call.respond(HttpStatusCode.OK)
        }
    }
}