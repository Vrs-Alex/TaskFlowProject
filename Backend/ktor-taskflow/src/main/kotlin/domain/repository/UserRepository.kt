package vrsalex.domain.repository

import vrsalex.domain.model.User
import vrsalex.domain.model.UserCreate

interface UserRepository {

    suspend fun findById(id: Long): User?
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    suspend fun create(user: UserCreate): Long
    suspend fun updatePassword(id: Long, newHash: String): Boolean


}