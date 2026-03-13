package vrsalex.core.security

import kotlin.uuid.Uuid

interface UserIdProvider {

    suspend fun getInternalId(publicId: Uuid): Long?

}