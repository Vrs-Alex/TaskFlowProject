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

@JvmInline
value class Username(val value: String) {
    init {
        require(value.length in 3..255 && REGEX.matches(value)) {
            throw AppException.InvalidFormat(value)
        }
    }

    companion object {
        private val REGEX = Regex("^[A-Za-zА-Яа-я0-9-_]+$")
    }
}
