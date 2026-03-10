package feature.account.domain.service

import feature.account.exception.AccountException
import vrsalex.core.database.transaction.TransactionWrapper
import vrsalex.core.security.PasswordHasher
import vrsalex.core.security.TokenType
import feature.account.domain.repository.RefreshTokenRepository
import feature.account.domain.model.JwtTokens
import feature.account.domain.model.RawPassword
import feature.account.domain.model.RefreshTokenCreate
import feature.account.domain.model.UserCreate
import feature.account.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.uuid.Uuid


class AccountService(
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
                throw AccountException.UserAlreadyExists()
            }

            val hashedPassword = passwordHasher.hash(RawPassword(data.password).value)

            val (userId, userPublicId) = userRepository.create(data.copy(password = hashedPassword))
            val jwtResult = jwtProvider.createTokens(userPublicId.toString())
            saveRefreshToken(userId, jwtResult.refreshTokenId,  jwtResult.refreshToken)

            return@dbTransaction JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
        }
    }


    suspend fun login(identity: String, password: String): JwtTokens = transactionWrapper.dbTransaction {
        val user = userRepository.findByUsername(identity)
                ?: userRepository.findByEmail(identity)
                ?: throw AccountException.InvalidCredentials()


        if (!passwordHasher.check(password, user.passwordHash)) {
            throw AccountException.InvalidCredentials()
        }

        val jwtResult = jwtProvider.createTokens(user.publicId.toString())
        saveRefreshToken(user.id, jwtResult.refreshTokenId,  jwtResult.refreshToken)
        JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
    }



    suspend fun refreshToken(refreshToken: String): JwtTokens = transactionWrapper.dbTransaction {
        val tokenId = jwtProvider.extractTokenId(refreshToken, TokenType.REFRESH)
            ?: throw AccountException.InvalidRefreshToken()

        val tokenRecord = refreshTokenRepository.findById(tokenId)
            ?: throw AccountException.InvalidRefreshToken()

        if (tokenRecord.expiresAt < Clock.System.now()) {
            refreshTokenRepository.deleteByTokenId(tokenId)
            throw AccountException.RefreshTokenExpired()
        }

        val user = userRepository.findById(tokenRecord.userId) ?: throw AccountException.UserNotFound()
        val jwtResult = jwtProvider.createTokens(user.publicId.toString())

        refreshTokenRepository.deleteByTokenId(tokenId)
        saveRefreshToken(user.id, jwtResult.refreshTokenId,  jwtResult.refreshToken)

        JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
    }



    // TODO deviceInfo, ipAddress
    private suspend fun saveRefreshToken(userId: Long, tokenId: Uuid, token: String) {
        val hashedToken = passwordHasher.hash(token)
        refreshTokenRepository.save(
            RefreshTokenCreate(
                tokenId = tokenId,
                userId = userId,
                tokenHash = hashedToken,
                deviceInfo = "",
                ipAddress = "",
                expiresAt = Clock.System.now() + 30.days
            )
        )
    }

}