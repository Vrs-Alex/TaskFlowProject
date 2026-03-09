package vrsalex.feature.auth.domain.repository

import vrsalex.feature.auth.domain.model.User
import vrsalex.feature.auth.domain.model.UserCreate
import kotlin.uuid.Uuid

interface UserRepository {

    suspend fun existsByUsername(username: String): Boolean
    suspend fun existsByEmail(email: String): Boolean
    suspend fun findById(id: Long): User?
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    suspend fun create(user: UserCreate): Pair<Long, Uuid>
    suspend fun updatePassword(id: Long, newHash: String): Boolean

}