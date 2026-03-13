package vrsalex.feature.account.data.repository

import com.github.benmanes.caffeine.cache.Caffeine
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.security.UserIdProvider
import vrsalex.feature.account.domain.repository.UserRepository
import java.time.Duration
import kotlin.uuid.Uuid

class CachedUserIdProvider(
    private val userRepository: UserRepository,
    private val transactionManager: TransactionManager
) : UserIdProvider {

    private val cache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(30))
        .build<Uuid, Long>()

    override suspend fun getInternalId(publicId: Uuid): Long? {
        return cache.getIfPresent(publicId) ?: transactionManager.dbTransaction {
            userRepository.findIdByPublicId(publicId)?.also {
                cache.put(publicId, it)
            }
        }
    }
}