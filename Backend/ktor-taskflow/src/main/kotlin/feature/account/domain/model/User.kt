package feature.account.domain.model


import vrsalex.core.value_object.Email
import vrsalex.core.value_object.Username
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ExperimentalUuidApi
data class User(
    val id: Long,
    val publicId: Uuid,
    val username: Username,
    val email: Email,
    val fullName: String?,
    val passwordHash: String,
    val createdAt: Instant
)

data class UserCreate(
    val username: Username,
    val email: Email,
    val fullName: String?,
    val password: String
)