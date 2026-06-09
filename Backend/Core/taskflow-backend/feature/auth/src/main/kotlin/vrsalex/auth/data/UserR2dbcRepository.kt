package vrsalex.auth.data

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import vrsalex.auth.domain.model.User
import vrsalex.auth.domain.model.UserCreate
import vrsalex.auth.domain.repository.UserRepository
import vrsalex.core.database.UserTable
import vrsalex.core.database.utils.exists
import vrsalex.core.database.utils.findOne
import vrsalex.core.exception.AppException
import kotlin.uuid.Uuid

internal class UserR2dbcRepository: UserRepository {

    override suspend fun existsByUsername(username: String): Boolean =
        UserTable.exists { UserTable.username eq username }

    override suspend fun existsByEmail(email: String): Boolean =
        UserTable.exists { UserTable.email eq email }

    override suspend fun existsByEmailOrUsername(email: String, username: String): Boolean {
        return UserTable.exists{
            (UserTable.email eq email) or (UserTable.username eq username)
        }
    }

    override suspend fun findIdByPublicId(publishId: Uuid): Long? =
        UserTable.findOne{ UserTable.publicId eq publishId }?.toUser()?.id

    override suspend fun findById(id: Long): User? =
        UserTable.findOne { UserTable.id eq id }?.toUser()

    override suspend fun findByUsername(username: String): User? =
        UserTable.findOne { UserTable.username eq username }?.toUser()

    override suspend fun findByEmail(email: String): User? =
        UserTable.findOne { UserTable.email eq email }?.toUser()

    override suspend fun create(user: UserCreate): Pair<Long, Uuid> = suspendTransaction {
        val publicId = Uuid.random()
        val id = UserTable.insertAndGetId {
            it[UserTable.publicId] = publicId
            it[username] = user.username.value
            it[email] = user.email.value
            it[fullName] = user.fullName
            it[UserTable.passwordHash] = user.hashedPassword ?: throw AppException.InternalServerError("Упс. Что-то пошло не так")
        }.value
        (id to publicId)
    }

    override suspend fun updatePassword(id: Long, newHash: String): Boolean {
        TODO("Not yet implemented")
    }
}