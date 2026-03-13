package vrsalex.feature.account.web

import vrsalex.api.dto.auth.RegisterRequest
import vrsalex.core.value_object.Email
import vrsalex.core.value_object.Username
import vrsalex.feature.account.domain.model.UserCreate

fun RegisterRequest.toUserCreate() = UserCreate(
    username = Username(this.username),
    email = Email(this.email),
    fullName = this.fullName,
    password = this.password
)