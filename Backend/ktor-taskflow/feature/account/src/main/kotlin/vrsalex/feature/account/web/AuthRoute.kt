package vrsalex.feature.account.web


import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import vrsalex.api.dto.account.*
import vrsalex.core.routing.FeatureRouter
import vrsalex.core.routing.RateLimitNames
import vrsalex.core.routing.RouteProtection
import vrsalex.core.routing.protected
import vrsalex.feature.account.domain.service.AccountService

class AuthRouter(private val accountService: AccountService) : FeatureRouter {

    override fun Route.registerRoutes() {

        protected(
            protection = RouteProtection.NONE,
            rateLimitName = RateLimitNames.LOGIN_AND_REGISTER
        ) {
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
}