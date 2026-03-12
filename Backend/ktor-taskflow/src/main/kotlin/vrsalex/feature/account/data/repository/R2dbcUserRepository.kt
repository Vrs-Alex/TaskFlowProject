package vrsalex.feature.account.data.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import vrsalex.core.database.utils.exists
import vrsalex.core.database.utils.findOne
import vrsalex.core.database.entity.AppUserTable
import vrsalex.feature.account.data.mapper.toUser

import vrsalex.feature.account.domain.model.User
import vrsalex.feature.account.domain.model.UserCreate
import vrsalex.feature.account.domain.repository.UserRepository
import kotlin.uuid.Uuid


class R2dbcUserRepository: UserRepository {

    override suspend fun existsByUsername(username: String): Boolean =
        AppUserTable.exists { AppUserTable.username eq username }

    override suspend fun existsByEmail(email: String): Boolean =
        AppUserTable.exists { AppUserTable.email eq email }

    override suspend fun existsByEmailOrUsername(email: String, username: String): Boolean {
        return AppUserTable.exists{
            (AppUserTable.email eq email) or (AppUserTable.username eq username)
        }
    }

    override suspend fun findIdByPublicId(publishId: Uuid): Long? =
        AppUserTable.findOne{ AppUserTable.publicId eq publishId }?.toUser()?.id

    override suspend fun findById(id: Long): User? =
        AppUserTable.findOne { AppUserTable.id eq id }?.toUser()

    override suspend fun findByUsername(username: String): User? =
        AppUserTable.findOne { AppUserTable.username eq username }?.toUser()

    override suspend fun findByEmail(email: String): User? =
        AppUserTable.findOne { AppUserTable.email eq email }?.toUser()

    override suspend fun create(user: UserCreate): Pair<Long, Uuid> = suspendTransaction {
        val publicId = Uuid.random()
        val id = AppUserTable.insertAndGetId {
            it[AppUserTable.publicId] = publicId
            it[username] = user.username.value
            it[email] = user.email.value
            it[fullName] = user.fullName
            it[AppUserTable.passwordHash] = user.password
        }.value
        (id to publicId)
    }

    override suspend fun updatePassword(id: Long, newHash: String): Boolean {
        TODO("Not yet implemented")
    }
}

