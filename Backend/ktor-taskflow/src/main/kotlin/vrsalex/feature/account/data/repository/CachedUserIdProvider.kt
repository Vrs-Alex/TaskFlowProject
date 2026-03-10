package vrsalex.feature.account.data.repository

import com.github.benmanes.caffeine.cache.Caffeine
import vrsalex.core.security.UserIdProvider
import vrsalex.feature.account.domain.repository.UserRepository
import java.time.Duration
import kotlin.uuid.Uuid

class CachedUserIdProvider(
    private val userRepository: UserRepository
) : UserIdProvider {

    private val cache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(30))
        .build<Uuid, Long>()

    override suspend fun getInternalId(publicId: Uuid): Long? {
        return cache.getIfPresent(publicId) ?: userRepository.findIdByPublicId(publicId)?.also {
            cache.put(publicId, it)
        }
    }

}