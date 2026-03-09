package vrsalex.feature.auth.web

import dto.auth.AuthResponse
import dto.auth.LoginRequest
import dto.auth.RegisterRequest
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import vrsalex.feature.auth.domain.service.AuthService
import vrsalex.feature.auth.web.mapper.toUserCreate

fun Route.authRoute() {

    val authService by inject<AuthService>()

    rateLimit(RateLimitName("auth_rate")) {

        post("/register") {
            val request = call.receive<RegisterRequest>()

            val tokens = authService.register(request.toUserCreate())

            call.respond(AuthResponse(tokens.accessToken, tokens.refreshToken))
        }

        post("/login") {
            val request = call.receive<LoginRequest>()

            val tokens = authService.login(request.identity, request.password)
            call.respond(AuthResponse(tokens.accessToken, tokens.refreshToken))
        }

    }

    post("/refresh-token") {

    }

}