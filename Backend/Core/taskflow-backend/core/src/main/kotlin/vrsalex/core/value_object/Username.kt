package vrsalex.core.value_object

import vrsalex.core.exception.AppException
import vrsalex.core.exception.ensure


@JvmInline
value class Username(val value: String) {
    init {
        ensure(value.length in 3..30 && REGEX.matches(value)) {
            throw AppException.InvalidFormat("Имя пользователя должно быть длиной от 3 до 30. Допустимы символы латинских букв, цифры, _ и -")
        }
    }

    companion object {
        private val REGEX = Regex("^[A-Za-z0-9_-]+$")
    }
}
