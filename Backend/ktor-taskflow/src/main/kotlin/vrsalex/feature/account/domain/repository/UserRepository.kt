package vrsalex.feature.account.domain.repository

import vrsalex.feature.account.domain.model.User
import vrsalex.feature.account.domain.model.UserCreate
import kotlin.uuid.Uuid

interface UserRepository {

    suspend fun existsByUsername(username: String): Boolean
    suspend fun existsByEmail(email: String): Boolean
    suspend fun existsByEmailOrUsername(email: String, username: String): Boolean

    suspend fun findIdByPublicId(publishId: Uuid): Long?
    suspend fun findById(id: Long): User?
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    suspend fun create(user: UserCreate): Pair<Long, Uuid>
    suspend fun updatePassword(id: Long, newHash: String): Boolean

}