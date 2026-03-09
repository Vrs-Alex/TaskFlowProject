package vrsalex.feature.auth.data.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import vrsalex.core.database.exists
import vrsalex.core.database.findOne
import vrsalex.feature.auth.data.table.AppUserTable
import vrsalex.feature.auth.data.mapper.toUser

import vrsalex.feature.auth.domain.model.User
import vrsalex.feature.auth.domain.model.UserCreate
import vrsalex.feature.auth.domain.repository.UserRepository
import kotlin.uuid.Uuid


class R2dbcUserRepository(
    private val db: R2dbcDatabase
): UserRepository {
    override suspend fun existsByUsername(username: String): Boolean =
        AppUserTable.exists(db) { AppUserTable.username eq username }

    override suspend fun existsByEmail(email: String): Boolean =
        AppUserTable.exists(db) { AppUserTable.email eq email }

    override suspend fun findById(id: Long): User? =
        AppUserTable.findOne(db) { AppUserTable.id eq id }?.toUser()

    override suspend fun findByUsername(username: String): User? =
        AppUserTable.findOne(db) { AppUserTable.username eq username }?.toUser()

    override suspend fun findByEmail(email: String): User? =
        AppUserTable.findOne(db) { AppUserTable.email eq email }?.toUser()

    override suspend fun create(user: UserCreate): Pair<Long, Uuid> {
        val publicId = Uuid.random()
        val id = AppUserTable.insertAndGetId {
            it[AppUserTable.publicId] = publicId
            it[username] = user.username.value
            it[email] = user.email.value
            it[fullName] = user.fullName
            it[AppUserTable.passwordHash] = user.password
        }.value
        return (id to publicId)
    }

    override suspend fun updatePassword(id: Long, newHash: String): Boolean {
        TODO("Not yet implemented")
    }
}

