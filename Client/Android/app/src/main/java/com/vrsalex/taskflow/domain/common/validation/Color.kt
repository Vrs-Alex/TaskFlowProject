package com.vrsalex.taskflow.domain.common.validation

@JvmInline
value class Color(val value: String) {
    companion object {
        fun of(raw: String): ValidationResult<Color>{
            val regex = "^#[A-Fa-f0-9]{6}$".toRegex()
            return when {
                regex.matches(raw) -> ValidationResult.Valid(Color(raw))
                else -> ValidationResult.Invalid(FieldError.INVALID_COLOR)
            }
        }
    }
}