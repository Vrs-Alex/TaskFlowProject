package vrsalex.core.value_object

@JvmInline
value class Color(val value: String) {
    init {
        require(value.matches(HEX_REGEX)) { "Неверный HEX для цвета" }
    }

    companion object {
        private val HEX_REGEX = "^#[A-Fa-f0-9]{6}$".toRegex()

        val Default = Color("#FFFFFF")
    }
}