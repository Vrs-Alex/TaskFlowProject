package vrsalex.feature.auth.web.mapper

import dto.auth.RegisterRequest
import vrsalex.core.Email
import vrsalex.core.Username
import vrsalex.feature.auth.domain.model.UserCreate

fun RegisterRequest.toUserCreate() = UserCreate(
    username = Username(this.username),
    email = Email(this.email),
    fullName = this.fullName,
    password = this.password
)