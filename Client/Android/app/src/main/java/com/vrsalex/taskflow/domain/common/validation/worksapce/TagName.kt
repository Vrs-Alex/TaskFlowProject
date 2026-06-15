package com.vrsalex.taskflow.domain.common.validation.worksapce

import com.vrsalex.taskflow.domain.common.validation.FieldError
import com.vrsalex.taskflow.domain.common.validation.ValidationResult

@JvmInline
value class TagName private constructor(val value: String) {
    companion object {
        const val MAX_LENGTH = 100
        fun of(raw: String): ValidationResult<TagName> {
            val name = raw.trim()
            return when {
                name.isEmpty()          -> ValidationResult.Invalid(FieldError.BLANK)
                name.length > MAX_LENGTH -> ValidationResult.Invalid(FieldError.TOO_LONG)
                else                    -> ValidationResult.Valid(TagName(name))
            }
        }
    }
}