package vrsalex.core.security.user

import kotlin.uuid.Uuid

data class UserPrincipal(
    val internalId: Long,
    val publicId: Uuid
)
