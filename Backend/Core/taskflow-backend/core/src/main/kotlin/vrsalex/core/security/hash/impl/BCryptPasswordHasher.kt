package vrsalex.core.security.hash.impl

import org.mindrot.jbcrypt.BCrypt
import vrsalex.core.security.hash.PasswordHasher

class BCryptPasswordHasher : PasswordHasher {
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