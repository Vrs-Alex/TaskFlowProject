package vrsalex.core.security.hash

interface PasswordHasher {
    fun hash(password: String): String
    fun check(password: String, hashed: String): Boolean
}