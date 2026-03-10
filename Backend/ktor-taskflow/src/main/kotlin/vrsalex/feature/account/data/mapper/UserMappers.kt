package vrsalex.feature.account.data.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.core.value_object.Email
import vrsalex.core.value_object.Username
import vrsalex.core.database.entity.AppUserTable
import vrsalex.feature.account.domain.model.User
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