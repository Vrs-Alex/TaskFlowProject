package vrsalex.core.value_object

import vrsalex.core.exception.AppException


@JvmInline
value class UserPassword(val value: String) {
    init {
        require(
            value.length in 6..128
                    && REGEX.matches(value)
                    && value.any { it.isUpperCase() }
                    && value.any { it.isDigit() }
                    && value.any { it.isLowerCase() }
        ) {
            throw AppException.InvalidFormat("Пароль должен быть длиной от 6 до 128, иметь хотя бы одну заглавную букву и одну цифру")
        }
    }

    companion object {
        private val REGEX = Regex("^[A-Za-z0-9-_!@#$%^&*()]+$")
    }
}
