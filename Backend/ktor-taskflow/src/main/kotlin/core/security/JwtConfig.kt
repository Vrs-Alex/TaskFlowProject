package core.security

class JwtConfig(
    val issuer: String,
    val audience: String,
    val expiration: Long,
    val refreshExpiration: Long,
    val realm: String,
    val secret: String
)