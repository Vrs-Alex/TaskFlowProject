package vrsalex.core.security

import kotlin.uuid.Uuid

data class UserPrincipal(
    val internalId: Long,
    val publicId: Uuid
)
