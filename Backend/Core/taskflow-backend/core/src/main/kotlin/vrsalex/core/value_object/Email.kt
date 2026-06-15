package vrsalex.core.value_object

import vrsalex.core.exception.AppException
import vrsalex.core.exception.ensure
import vrsalex.shared.api.exception.ErrorCode

@JvmInline
value class Email(val value: String) {
    init {
        ensure(REGEX.matches(value), ErrorCode.EMAIL_VALIDATION_FAILED)
    }

    companion object {
        private val REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    }
}
