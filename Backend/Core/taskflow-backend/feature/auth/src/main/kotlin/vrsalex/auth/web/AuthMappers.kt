package vrsalex.auth.web

import vrsalex.auth.domain.model.UserCreate
import vrsalex.core.value_object.Email
import vrsalex.core.value_object.UserPassword
import vrsalex.core.value_object.Username
import vrsalex.shared.api.auth.RegisterRequest

fun RegisterRequest.toUserCreate() = UserCreate(
    username = Username(this.username),
    email = Email(this.email),
    fullName = this.fullName,
    password = UserPassword(this.password)
)