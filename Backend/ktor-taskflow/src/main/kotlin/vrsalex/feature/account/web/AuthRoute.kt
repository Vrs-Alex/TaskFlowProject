package vrsalex.feature.account.web

import dto.auth.AuthResponse
import dto.auth.LoginRequest
import dto.auth.RefreshTokenRequest
import dto.auth.RegisterRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import vrsalex.app.plugin.RateLimitNames
import vrsalex.feature.account.domain.service.AccountService

fun Route.authRoute() {

    val accountService by inject<AccountService>()

    rateLimit(RateLimitName(RateLimitNames.LOGIN_AND_REGISTER.name)) {

        post("/register") {
            val request = call.receive<RegisterRequest>()

            val tokens = accountService.register(request.toUserCreate())

            call.respond(HttpStatusCode.Created, AuthResponse(tokens.accessToken, tokens.refreshToken))
        }

        post("/login") {
            val request = call.receive<LoginRequest>()

            val tokens = accountService.login(request.identity, request.password)
            call.respond(AuthResponse(tokens.accessToken, tokens.refreshToken))
        }

    }


    post("/refresh-token") {
        val request = call.receive<RefreshTokenRequest>()

        val tokens = accountService.refreshToken(request.token)

        call.respond(AuthResponse(tokens.accessToken, tokens.refreshToken))
    }

}