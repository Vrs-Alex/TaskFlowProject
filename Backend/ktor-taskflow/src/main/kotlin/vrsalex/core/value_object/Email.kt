package vrsalex.core.value_object

import vrsalex.core.exception.AppException

@JvmInline
value class Email(val value: String) {
    init {
        require(REGEX.matches(value)) {
            throw AppException.InvalidFormat(value)
        }
    }

    companion object {
        private val REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    }
}
