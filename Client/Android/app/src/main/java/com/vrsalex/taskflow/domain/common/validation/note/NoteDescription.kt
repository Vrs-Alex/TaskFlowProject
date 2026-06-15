package com.vrsalex.taskflow.domain.common.validation.note

import com.vrsalex.taskflow.domain.common.validation.FieldError
import com.vrsalex.taskflow.domain.common.validation.ValidationResult

@JvmInline
value class NoteDescription private constructor(val value: String) {
    companion object {
        const val MAX_LENGTH = 5000

        /** Реконструкция из доверенного источника (БД/сервер) — без повторной валидации. */
        fun trusted(raw: String): NoteDescription = NoteDescription(raw)

        fun of(raw: String): ValidationResult<NoteDescription> {
            val name = raw.trim()
            return when {
                name.isEmpty()          -> ValidationResult.Invalid(FieldError.BLANK)
                name.length > MAX_LENGTH -> ValidationResult.Invalid(FieldError.TOO_LONG)
                else                    -> ValidationResult.Valid(NoteDescription(name))
            }
        }
    }
}