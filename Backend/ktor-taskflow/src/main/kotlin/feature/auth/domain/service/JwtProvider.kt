package vrsalex.feature.auth.domain.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import vrsalex.core.security.TokenType
import vrsalex.feature.auth.domain.model.JwtConfig
import java.util.Date

class JwtProvider(
    private val config: JwtConfig
) {

    fun createTokens(userPublicId: String): Pair<String, String> {
        val accessToken = createToken(userPublicId, TokenType.ACCESS)
        val refreshToken = createToken(userPublicId, TokenType.REFRESH)
        return Pair(accessToken, refreshToken)
    }

    private fun createToken(
        userPublicId: String,
        tokenType: TokenType
    ): String {
        val expirationTime = when (tokenType) {
            TokenType.ACCESS -> config.expiration
            TokenType.REFRESH -> config.refreshExpiration
        }

        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("user_id", userPublicId)
            .withClaim("type", tokenType.name)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .sign(Algorithm.HMAC256(config.secret))
    }

    private fun getVerifier() = JWT
        .require(Algorithm.HMAC256(config.secret))
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .build()


    fun verifyAccessToken(token: String): DecodedJWT? {
        return try {
            val decoded = getVerifier().verify(token)
            if (decoded.getClaim("type").asString() == TokenType.ACCESS.name) decoded else null
        } catch (e: Exception) {
            null
        }
    }

    fun verifyRefreshToken(token: String): DecodedJWT? {
        return try {
            val decoded = getVerifier().verify(token)
            if (decoded.getClaim("type").asString() == TokenType.REFRESH.name) decoded else null
        } catch (e: Exception) {
            null
        }
    }


}