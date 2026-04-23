package com.vrsalex.taskflow.domain.auth

import com.vrsalex.taskflow.domain.common.Resource

interface AuthRepository {

    suspend fun signIn(identity: String, password: String): Resource<Unit>

    suspend fun signUp(data: SignUpData): Resource<Unit>

}