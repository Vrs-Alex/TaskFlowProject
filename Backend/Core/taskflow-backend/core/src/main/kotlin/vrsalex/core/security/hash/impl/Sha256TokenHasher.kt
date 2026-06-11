package vrsalex.core.security.hash.impl

import vrsalex.core.security.hash.TokenHasher
import java.security.MessageDigest

class Sha256TokenHasher : TokenHasher {
    override fun hash(token: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(token.toByteArray())
            .joinToString("") { "%02x".format(it) }
}