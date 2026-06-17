package com.vrsalex.taskflow.domain.common.validation.worksapce

import com.vrsalex.taskflow.domain.common.validation.FieldError
import com.vrsalex.taskflow.domain.common.validation.ValidationResult

@JvmInline
value class AreaName private constructor(val value: String) {
    companion object {
        const val MAX_LENGTH = 100

        /** Реконструкция из доверенного источника (БД/сервер) — без повторной валидации. */
        fun trusted(raw: String): AreaName = AreaName(raw)

        fun of(raw: String): ValidationResult<AreaName> {
            val name = raw.trim()
            return when {
                name.isEmpty()          -> ValidationResult.Invalid(FieldError.BLANK)
                name.length > MAX_LENGTH -> ValidationResult.Invalid(FieldError.TOO_LONG)
                else                    -> ValidationResult.Valid(AreaName(name))
            }
        }
    }
}