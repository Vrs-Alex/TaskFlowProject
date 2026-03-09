package vrsalex.feature.auth.domain.service

import vrsalex.core.database.transaction.TransactionWrapper
import vrsalex.core.security.PasswordHasher
import vrsalex.feature.auth.domain.repository.RefreshTokenRepository
import vrsalex.feature.auth.domain.model.JwtTokens
import vrsalex.feature.auth.domain.model.RawPassword
import vrsalex.feature.auth.domain.model.RefreshTokenCreate
import vrsalex.feature.auth.exception.AuthException
import vrsalex.feature.auth.domain.model.User
import vrsalex.feature.auth.domain.model.UserCreate
import vrsalex.feature.auth.domain.repository.UserRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days


class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider,
    private val passwordHasher: PasswordHasher,
    private val transactionWrapper: TransactionWrapper
) {

    suspend fun register(data: UserCreate): JwtTokens  {
        return transactionWrapper.dbTransaction {
            if (userRepository.existsByEmail(data.email.value)
                || userRepository.existsByUsername(data.username.value)) {
                throw AuthException.UserAlreadyExists()
            }

            val hashedPassword = passwordHasher.hash(RawPassword(data.password).value)

            val (userId, userPublicId) = userRepository.create(data.copy(password = hashedPassword))
            val (accessToken, refreshToken) = jwtProvider.createTokens(userPublicId.toString())
            saveRefreshToken(userId, refreshToken)

            return@dbTransaction JwtTokens(accessToken, refreshToken)
        }
    }


    suspend fun login(identity: String, password: String): JwtTokens = transactionWrapper.dbTransaction {
        val user = userRepository.findByUsername(identity)
                ?: userRepository.findByEmail(identity)
                ?: throw AuthException.UserNotFound()


        if (!passwordHasher.check(password, user.passwordHash)) {
            throw AuthException.InvalidPassword()
        }

        val (accessToken, refreshToken) = jwtProvider.createTokens(user.publicId.toString())
        saveRefreshToken(user.id, refreshToken)
        JwtTokens(accessToken, refreshToken)
    }



    // TODO deviceInfo, ipAddress
    private suspend fun saveRefreshToken(userId: Long, token: String) {
        val hashedToken = passwordHasher.hash(token)
        refreshTokenRepository.save(
            RefreshTokenCreate(
                userId = userId,
                tokenHash = hashedToken,
                deviceInfo = "",
                ipAddress = "",
                expiresAt = Clock.System.now() + 30.days
            )
        )
    }

}