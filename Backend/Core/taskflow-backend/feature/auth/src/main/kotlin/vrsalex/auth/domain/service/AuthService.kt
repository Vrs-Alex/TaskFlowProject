package vrsalex.auth.domain.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vrsalex.auth.AuthException
import vrsalex.auth.domain.model.JwtTokens
import vrsalex.auth.domain.model.RefreshTokenCreate
import vrsalex.auth.domain.model.UserCreate
import vrsalex.auth.domain.repository.RefreshTokenRepository
import vrsalex.auth.domain.repository.UserRepository
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.security.hash.PasswordHasher
import vrsalex.core.security.jwt.JwtTokenType
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.uuid.Uuid


class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider,
    private val passwordHasher: PasswordHasher,
    private val transactionManager: TransactionManager
) {

    suspend fun register(data: UserCreate, ip: String, userAgent: String): JwtTokens {
        val hashedPassword = withContext(Dispatchers.Default){
            passwordHasher.hash(data.password.value)
        }

        return transactionManager.dbTransaction {
            if (userRepository.existsByEmailOrUsername(data.email.value, data.username.value)) {
                throw AuthException.UserAlreadyExists()
            }

            val (userId, userPublicId) = userRepository.create(data.copy(hashedPassword = hashedPassword))

            val jwtResult = jwtProvider.createTokens(userPublicId.toString())

            saveRefreshToken(userId, jwtResult.refreshTokenId, jwtResult.refreshToken, ip, userAgent)

            JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
        }
    }



    suspend fun login(identity: String, password: String, ip: String, userAgent: String): JwtTokens {
        val user = transactionManager.dbTransaction {
            userRepository.findByUsername(identity)
                ?: userRepository.findByEmail(identity)
        } ?: throw AuthException.InvalidCredentials()

        if (!passwordHasher.check(password, user.passwordHash)) {
            throw AuthException.InvalidCredentials()
        }
        val jwtResult = jwtProvider.createTokens(user.publicId.toString())

        transactionManager.dbTransaction {
            saveRefreshToken(user.id, jwtResult.refreshTokenId, jwtResult.refreshToken, ip, userAgent)
        }

        return JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
    }



    suspend fun refreshToken(refreshToken: String, ip: String, userAgent: String): JwtTokens {
        val tokenId = jwtProvider.extractTokenId(refreshToken, JwtTokenType.REFRESH)
            ?: throw AuthException.InvalidRefreshToken().also { println(2) }

        return transactionManager.dbTransaction {
            val tokenRecord = refreshTokenRepository.findById(tokenId)
                ?: throw AuthException.InvalidRefreshToken().also { println(tokenId) }

            if (tokenRecord.expiresAt < Clock.System.now()) {
                refreshTokenRepository.deleteByTokenId(tokenId)
                throw AuthException.RefreshTokenExpired()
            }

            val user = userRepository.findById(tokenRecord.userId)
                ?: throw AuthException.UserNotFound()

            val jwtResult = jwtProvider.createTokens(user.publicId.toString())

            refreshTokenRepository.deleteByTokenId(tokenId)
            saveRefreshToken(user.id, jwtResult.refreshTokenId, jwtResult.refreshToken, ip, userAgent)

            JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
        }
    }



    private suspend fun saveRefreshToken(userId: Long, tokenId: Uuid, token: String, ip: String, userAgent: String) {
        val hashedToken = passwordHasher.hash(token)
        refreshTokenRepository.save(
            RefreshTokenCreate(
                tokenId = tokenId,
                userId = userId,
                tokenHash = hashedToken,
                agent = userAgent,
                ipAddress = ip,
                expiresAt = Clock.System.now() + 30.days
            )
        )
    }

}