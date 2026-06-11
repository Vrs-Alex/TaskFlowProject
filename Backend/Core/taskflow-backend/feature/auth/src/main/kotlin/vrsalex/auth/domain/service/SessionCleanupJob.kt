package vrsalex.auth.domain.service

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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