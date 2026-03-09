package vrsalex.core.security

import org.mindrot.jbcrypt.BCrypt

class BCryptHasher : PasswordHasher {
    override fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }

    override fun check(password: String, hashed: String): Boolean {
        return try {
            BCrypt.checkpw(password, hashed)
        } catch (e: Exception) {
            false
        }
    }
}