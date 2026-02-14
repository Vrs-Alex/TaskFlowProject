package vrsalex.infrastructure.persistence.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.domain.model.User
import vrsalex.infrastructure.persistence.table.AppUserTable

fun ResultRow.toUser() = User(
    id = this[AppUserTable.id].value,
    username = this[AppUserTable.username],
    email = this[AppUserTable.email],
    fullName = this[AppUserTable.fullName],
    createdAt = this[AppUserTable.createdAt]
)