package com.vrsalex.taskflow.domain.auth

import com.vrsalex.taskflow.domain.common.model.Resource

interface AuthRepository {

    suspend fun signIn(identity: String, password: String): Resource<Unit>

    suspend fun signUp(data: SignUpData): Resource<Unit>

    suspend fun upsertDevice(data: DeviceInfo): Resource<Unit>

    suspend fun registerDevice(token: String? = null): Resource<Unit>

}