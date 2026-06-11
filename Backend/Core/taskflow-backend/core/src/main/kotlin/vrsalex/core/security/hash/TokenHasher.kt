package vrsalex.core.security.hash

interface TokenHasher {
    fun hash(token: String): String
}

