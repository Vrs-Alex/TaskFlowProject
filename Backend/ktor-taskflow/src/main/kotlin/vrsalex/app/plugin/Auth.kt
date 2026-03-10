package vrsalex.app.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.get
import vrsalex.core.security.JwtConfig
import vrsalex.core.security.JwtTokenType
import vrsalex.core.security.UserIdProvider
import vrsalex.core.security.UserPrincipal
import kotlin.uuid.Uuid

fun Application.configureAuth() {
    val config = get<JwtConfig>()
    val userIdProvider = get<UserIdProvider>()

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

                if (tokenType == JwtTokenType.ACCESS.name) {
                    val publicId = credential.payload.getClaim("user_id").asString()
                    val internalUserId = userIdProvider.getInternalId(Uuid.parse(publicId))
                        ?: return@validate null

                    UserPrincipal(internalUserId, Uuid.parse(publicId))
                } else {
                    null
                }
            }
        }
    }
}
