package feature.account.domain.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import vrsalex.core.security.TokenType
import core.security.JwtConfig
import feature.account.domain.model.TokenGenerationResult
import java.util.Date
import kotlin.uuid.Uuid

class JwtProvider(
    private val config: JwtConfig
) {

    fun createTokens(userPublicId: String): TokenGenerationResult {
        val (accessTokenId, accessToken) = createToken(userPublicId, TokenType.ACCESS)
        val (refreshTokenId, refreshToken) = createToken(userPublicId, TokenType.REFRESH)
        return TokenGenerationResult(accessToken, refreshToken, refreshTokenId)
    }


    fun extractTokenId(token: String, type: TokenType): Uuid? {
        val decodedJwt = verifyToken(token, type)
        return decodedJwt?.let { Uuid.parse(it.id) }
    }


    private fun verifyToken(token: String, type: TokenType): DecodedJWT? {
        return try {
            val decoded = getVerifier().verify(token)
            if (decoded.getClaim("type").asString() == type.name) decoded else null
        } catch (e: Exception) {
            null
        }
    }

    private fun createToken(
        userPublicId: String,
        tokenType: TokenType
    ): Pair<Uuid, String> {
        val expirationTime = when (tokenType) {
            TokenType.ACCESS -> config.expiration
            TokenType.REFRESH -> config.refreshExpiration
        }
        val tokenId = Uuid.random()
        val token =  JWT.create()
            .withJWTId(tokenId.toString())
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("user_id", userPublicId)
            .withClaim("type", tokenType.name)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .sign(Algorithm.HMAC256(config.secret))
        return (tokenId to token)
    }

    private fun getVerifier() = JWT
        .require(Algorithm.HMAC256(config.secret))
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .build()

}