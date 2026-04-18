package vrsalex.auth.data

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.auth.domain.model.User
import vrsalex.core.database.UserTable
import vrsalex.core.value_object.Email
import vrsalex.core.value_object.Username
import kotlin.uuid.ExperimentalUuidApi


fun ResultRow.toUser() = User(
    id = this[UserTable.id].value,
    publicId = this[UserTable.publicId],
    username = Username(this[UserTable.username]),
    email = Email(this[UserTable.email]),
    fullName = this[UserTable.fullName],
    passwordHash = this[UserTable.passwordHash],
    createdAt = this[UserTable.createdAt]
)