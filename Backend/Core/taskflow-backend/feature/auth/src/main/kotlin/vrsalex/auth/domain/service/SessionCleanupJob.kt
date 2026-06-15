package vrsalex.auth.domain.service

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import vrsalex.auth.domain.repository.RefreshTokenRepository
import vrsalex.core.database.transaction.TransactionManager
import kotlin.time.Duration.Companion.hours

class SessionCleanupJob(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val transactionManager: TransactionManager,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
) {
    private val logger = LoggerFactory.getLogger(SessionCleanupJob::class.java)

    init {
        scope.launch {
            while (isActive) {
                try {
                    val deleted = transactionManager.dbTransaction {
                        refreshTokenRepository.deleteExpired()
                    }
                    logger.info("Session cleanup: removed $deleted expired sessions")
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    logger.error("Session cleanup failed", e)
                }
                delay(24.hours)
            }
        }
    }
}