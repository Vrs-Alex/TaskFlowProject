package com.vrsalex.taskflow.domain.common.validation.note

import com.vrsalex.taskflow.domain.common.validation.FieldError
import com.vrsalex.taskflow.domain.common.validation.ValidationResult

@JvmInline
value class NoteName private constructor(val value: String) {
    companion object {
        const val MAX_LENGTH = 255

        /** Реконструкция из доверенного источника (БД/сервер) — без повторной валидации. */
        fun trusted(raw: String): NoteName = NoteName(raw)

        fun of(raw: String): ValidationResult<NoteName> {
            val name = raw.trim()
            return when {
                name.isEmpty()          -> ValidationResult.Invalid(FieldError.BLANK)
                name.length > MAX_LENGTH -> ValidationResult.Invalid(FieldError.TOO_LONG)
                else                    -> ValidationResult.Valid(NoteName(name))
            }
        }
    }
}