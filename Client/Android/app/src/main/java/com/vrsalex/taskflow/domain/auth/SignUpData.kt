package com.vrsalex.taskflow.domain.auth

data class SignUpData(
    val email: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val fullName: String?
)
