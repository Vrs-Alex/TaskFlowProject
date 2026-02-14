package vrsalex.infrastructure.persistence.repository

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.select

import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import vrsalex.domain.model.User
import vrsalex.domain.model.UserCreate
import vrsalex.domain.repository.UserRepository
import vrsalex.infrastructure.persistence.mapper.toUser
import vrsalex.infrastructure.persistence.table.AppUserTable

class R2dbcUserRepository(
    private val db: R2dbcDatabase
): UserRepository {

    override suspend fun findById(id: Long): User? = suspendTransaction(db) {
        AppUserTable.selectAll()
            .where { AppUserTable.id eq id }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun findByUsername(username: String): User? = suspendTransaction(db) {
        AppUserTable.selectAll()
            .where { AppUserTable.username eq username }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun findByEmail(email: String): User? = suspendTransaction(db) {
        AppUserTable.selectAll()
            .where { AppUserTable.email eq email }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun create(user: UserCreate): Long =
        AppUserTable.insertAndGetId {
            it[username] = user.username
            it[email] = user.email
            it[fullName] = user.fullName
            it[AppUserTable.passwordHash] = user.passwordHash
        }.value


    override suspend fun updatePassword(id: Long, newHash: String): Boolean {
        TODO("Not yet implemented")
    }
}

