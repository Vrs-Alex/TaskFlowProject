package vrsalex.feature.account.domain.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.security.PasswordHasher
import vrsalex.core.security.JwtTokenType
import vrsalex.feature.account.AccountException
import vrsalex.feature.account.domain.repository.RefreshTokenRepository
import vrsalex.feature.account.domain.model.JwtTokens
import vrsalex.feature.account.domain.model.RefreshTokenCreate
import vrsalex.feature.account.domain.model.UserCreate
import vrsalex.feature.account.domain.repository.UserRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.uuid.Uuid


class AccountService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider,
    private val passwordHasher: PasswordHasher,
    private val transactionManager: TransactionManager
) {

    suspend fun register(data: UserCreate): JwtTokens {
        val hashedPassword = withContext(Dispatchers.Default){
            passwordHasher.hash(data.password)
        }

        return transactionManager.dbTransaction {
            if (userRepository.existsByEmailOrUsername(data.email.value, data.username.value)) {
                throw AccountException.UserAlreadyExists()
            }

            val (userId, userPublicId) = userRepository.create(data.copy(password = hashedPassword))

            val jwtResult = jwtProvider.createTokens(userPublicId.toString())

            saveRefreshToken(userId, jwtResult.refreshTokenId, jwtResult.refreshToken)

            JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
        }
    }



    suspend fun login(identity: String, password: String): JwtTokens {
        val user = transactionManager.dbTransaction {
            userRepository.findByUsername(identity)
                ?: userRepository.findByEmail(identity)
        } ?: throw AccountException.InvalidCredentials()

        if (!passwordHasher.check(password, user.passwordHash)) {
            throw AccountException.InvalidCredentials()
        }
        val jwtResult = jwtProvider.createTokens(user.publicId.toString())

        transactionManager.dbTransaction {
            saveRefreshToken(user.id, jwtResult.refreshTokenId, jwtResult.refreshToken)
        }

        return JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
    }



    suspend fun refreshToken(refreshToken: String): JwtTokens {
        val tokenId = jwtProvider.extractTokenId(refreshToken, JwtTokenType.REFRESH)
            ?: throw AccountException.InvalidRefreshToken()

        return transactionManager.dbTransaction {
            val tokenRecord = refreshTokenRepository.findById(tokenId)
                ?: throw AccountException.InvalidRefreshToken()

            if (tokenRecord.expiresAt < Clock.System.now()) {
                refreshTokenRepository.deleteByTokenId(tokenId)
                throw AccountException.RefreshTokenExpired()
            }

            val user = userRepository.findById(tokenRecord.userId)
                ?: throw AccountException.UserNotFound()

            val jwtResult = jwtProvider.createTokens(user.publicId.toString())

            refreshTokenRepository.deleteByTokenId(tokenId)
            saveRefreshToken(user.id, jwtResult.refreshTokenId, jwtResult.refreshToken)

            JwtTokens(jwtResult.accessToken, jwtResult.refreshToken)
        }
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