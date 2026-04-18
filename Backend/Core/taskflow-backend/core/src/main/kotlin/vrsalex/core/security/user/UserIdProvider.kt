package vrsalex.core.security.user

import kotlin.uuid.Uuid

interface UserIdProvider {

    suspend fun getInternalId(publicId: Uuid): Long?

}