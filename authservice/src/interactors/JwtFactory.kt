package com.phoenix.authservice.interactors

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.phoenix.authservice.config.Config
import com.phoenix.authservice.models.User
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

object JwtFactory {

    private val privateKey: RSAPrivateKey by lazy {
        var key = Config.jwtPrivateKey
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
        key = key.replace("-----END PRIVATE KEY-----", "")
        key = key.replace("\\n", "")

        val encodedKey = Base64.getDecoder().decode(key.toByteArray())
        val spec = PKCS8EncodedKeySpec(encodedKey)
        val keyFactory = KeyFactory.getInstance("RSA")
        keyFactory.generatePrivate(spec) as RSAPrivateKey
    }

    private val algorithm: Algorithm = Algorithm.RSA256(null, privateKey)
    private const val validityInMs = 1000 * 60 * 5      // 5 minutes
    private const val issuer = "phoenix.io"
    private const val audience = "userinfoservice"

    private fun getExpiration(): Date = Date(System.currentTimeMillis() + validityInMs)

    fun createToken(user: User): String = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withSubject(user.userId)
        .withExpiresAt(getExpiration())
        .withClaim("username", user.username)
        .sign(algorithm)
}
