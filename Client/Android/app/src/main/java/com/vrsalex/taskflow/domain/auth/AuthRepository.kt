package com.vrsalex.taskflow.domain.auth

import com.vrsalex.taskflow.domain.common.Resource

interface AuthRepository {

    suspend fun login(data: LoginData): Resource<AuthTokens>

    suspend fun register(data: RegisterData): Resource<AuthTokens>

}