package com.vrsalex.taskflow.domain.auth

data class AuthTokens(
    val access: String,
    val refresh: String
)
