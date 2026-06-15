package vrsalex.core.value_object

import vrsalex.core.exception.ensure
import vrsalex.shared.api.exception.ErrorCode

@JvmInline
value class Color(val value: String) {
    init {
        ensure(value.matches(HEX_REGEX), ErrorCode.INVALID_HEX_COLOR)
    }

    companion object {
        private val HEX_REGEX = "^#[A-Fa-f0-9]{6}$".toRegex()

        val Default = Color("#FFFFFF")
    }
}