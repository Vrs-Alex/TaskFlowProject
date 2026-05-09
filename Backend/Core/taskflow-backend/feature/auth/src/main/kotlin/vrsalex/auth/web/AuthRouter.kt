package vrsalex.auth.web

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.origin
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import vrsalex.auth.domain.model.UserLogin
import vrsalex.auth.domain.service.AuthService
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.RateLimitNames
import vrsalex.core.routing.RouteProtection
import vrsalex.core.routing.protected
import vrsalex.core.routing.realIp
import vrsalex.shared.api.auth.AuthResponse
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RefreshTokenRequest
import vrsalex.shared.api.auth.RegisterRequest

class AuthRouter(private val service: AuthService) : AppRouter {

    override fun Route.registerRoutes() {

        protected(
            protection = RouteProtection.NONE,
            rateLimitName = RateLimitNames.LOGIN
        ) {
            post("/auth/login") {
                val request = call.receive<LoginRequest>()
                val ip = call.realIp()
                val userAgent = call.request.headers["User-Agent"] ?: ""

                val data = UserLogin(
                    identity = request.identity,
                    password = request.password,
                    fcmToken = request.fcmToken,
                    ipAddress = ip,
                    agent = userAgent
                )

                val tokens = service.login(data)
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
                val userAgent = call.request.headers["User-Agent"] ?: ""
                val tokens = service.register(request.toUserCreate(), ip, userAgent)
                call.respond(HttpStatusCode.Created, AuthResponse(tokens.accessToken, tokens.refreshToken))
            }
        }

        post("/auth/refresh-token") {
            val request = call.receive<RefreshTokenRequest>()

            val ip = call.realIp()
            val userAgent = call.request.headers["User-Agent"] ?: ""
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