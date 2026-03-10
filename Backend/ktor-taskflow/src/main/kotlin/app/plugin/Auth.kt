package vrsalex.app.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.get
import core.security.JwtConfig
import vrsalex.core.security.TokenType

fun Application.configureAuth() {
    val config = get<JwtConfig>()

    authentication {
        jwt("auth-jwt") {
            realm = config.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                val tokenType = credential.payload.getClaim("type").asString()
                val publicId = credential.payload.getClaim("user_id").asString()

                if (tokenType == TokenType.ACCESS.name && publicId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}