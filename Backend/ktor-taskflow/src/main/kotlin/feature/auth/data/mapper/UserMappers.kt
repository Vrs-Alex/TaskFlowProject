package vrsalex.feature.auth.data.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.core.Email
import vrsalex.core.Username
import vrsalex.feature.auth.data.table.AppUserTable
import vrsalex.feature.auth.domain.model.User
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun ResultRow.toUser() = User(
    id = this[AppUserTable.id].value,
    publicId = this[AppUserTable.publicId],
    username = Username(this[AppUserTable.username]),
    email = Email(this[AppUserTable.email]),
    fullName = this[AppUserTable.fullName],
    passwordHash = this[AppUserTable.passwordHash],
    createdAt = this[AppUserTable.createdAt]
)