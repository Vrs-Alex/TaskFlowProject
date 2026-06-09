package com.vrsalex.taskflow.domain.profile

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun setPushEnabled(boolean: Boolean)

    fun isPushEnabled(): Flow<Boolean>

}