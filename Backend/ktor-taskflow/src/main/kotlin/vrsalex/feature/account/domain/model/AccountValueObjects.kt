package vrsalex.feature.account.domain.model


import vrsalex.feature.account.AccountException


@JvmInline
value class RawPassword(val value: String) {
    init {
        val isValid = value.length in 8..100 &&
                value.any { it.isUpperCase() } &&
                value.any { it.isLowerCase() } &&
                value.any { it.isDigit() } &&
                value.any { it in SPECIAL_CHARS }

        require(isValid) { throw AccountException.InvalidPassword() }
    }

    companion object {
        private const val SPECIAL_CHARS = "!@#$%^&*(),.?:{}|<>_"
    }
}


