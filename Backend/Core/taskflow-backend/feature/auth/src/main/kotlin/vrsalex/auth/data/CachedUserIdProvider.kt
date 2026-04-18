package vrsalex.auth.data

import com.github.benmanes.caffeine.cache.Caffeine
import vrsalex.auth.domain.repository.UserRepository
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.security.user.UserIdProvider
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