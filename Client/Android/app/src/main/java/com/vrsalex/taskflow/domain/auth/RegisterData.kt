package com.vrsalex.taskflow.domain.auth

data class RegisterData(
    val username: String,
    val email: String,
    val fullName: String?,
    val password: String
)
