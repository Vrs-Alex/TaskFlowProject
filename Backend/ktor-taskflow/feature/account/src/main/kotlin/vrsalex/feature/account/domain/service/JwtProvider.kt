package vrsalex.feature.account.domain.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import vrsalex.core.security.JwtTokenType
import vrsalex.core.security.JwtConfig
import vrsalex.feature.account.domain.model.TokenGenerationResult
import java.util.Date
import kotlin.uuid.Uuid

class JwtProvider(
    private val config: JwtConfig
) {

    fun createTokens(userPublicId: String): TokenGenerationResult {
        val (accessTokenId, accessToken) = createToken(userPublicId, JwtTokenType.ACCESS)
        val (refreshTokenId, refreshToken) = createToken(userPublicId, JwtTokenType.REFRESH)
        return TokenGenerationResult(accessToken, refreshToken, refreshTokenId)
    }


    fun extractTokenId(token: String, type: JwtTokenType): Uuid? {
        val decodedJwt = verifyToken(token, type)
        return decodedJwt?.let { Uuid.parse(it.id) }
    }


    private fun verifyToken(token: String, type: JwtTokenType): DecodedJWT? {
        return try {
            val decoded = getVerifier().verify(token)
            if (decoded.getClaim("type").asString() == type.name) decoded else null
        } catch (e: Exception) {
            null
        }
    }

    private fun createToken(
        userPublicId: String,
        jwtTokenType: JwtTokenType
    ): Pair<Uuid, String> {
        val expirationTime = when (jwtTokenType) {
            JwtTokenType.ACCESS -> config.expiration
            JwtTokenType.REFRESH -> config.refreshExpiration
        }
        val tokenId = Uuid.random()
        val token =  JWT.create()
            .withJWTId(tokenId.toString())
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("user_id", userPublicId)
            .withClaim("type", jwtTokenType.name)
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