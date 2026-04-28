package vrsalex.auth.web

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import vrsalex.auth.domain.service.AuthService
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.RateLimitNames
import vrsalex.core.routing.RouteProtection
import vrsalex.core.routing.protected
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
                val tokens = service.login(request.identity, request.password)
                call.respond(HttpStatusCode.OK, AuthResponse(tokens.accessToken, tokens.refreshToken))
            }
        }


        protected(
            protection = RouteProtection.NONE,
            rateLimitName = RateLimitNames.REGISTER
        ) {
            post("/auth/register") {
                val request = call.receive<RegisterRequest>()
                val tokens = service.register(request.toUserCreate())
                call.respond(HttpStatusCode.Created, AuthResponse(tokens.accessToken, tokens.refreshToken))
            }
        }

        post("/auth/refresh-token") {
            val request = call.receive<RefreshTokenRequest>()
            val tokens = service.refreshToken(request.token)
            call.respond(AuthResponse(tokens.accessToken, tokens.refreshToken))
        }
    }
}