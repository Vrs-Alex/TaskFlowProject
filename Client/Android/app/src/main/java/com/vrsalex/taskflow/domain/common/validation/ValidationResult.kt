package com.vrsalex.taskflow.domain.common.validation

sealed interface ValidationResult<out T> {
    data class Valid<T>(val value: T) : ValidationResult<T>
    data class Invalid(val error: FieldError) : ValidationResult<Nothing>
}

fun <T> ValidationResult<T>.getOrNull(): T? = (this as? ValidationResult.Valid)?.value

enum class FieldError { BLANK, TOO_LONG, INVALID_EMAIL, INVALID_COLOR }