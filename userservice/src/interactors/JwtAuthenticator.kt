package com.phoenix.userservice.interactors

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.phoenix.userservice.config.Config
import com.phoenix.userservice.models.UserPrincipal
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

object JwtAuthenticator {

    private val publicKey: RSAPublicKey by lazy {
        var key = Config.jwtPublicKey
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
        key = key.replace("-----END PUBLIC KEY-----", "")
        key = key.replace("\\n", "")

        val encodedKey = Base64.getDecoder().decode(key.toByteArray())
        val spec = X509EncodedKeySpec(encodedKey)
        val keyFactory = KeyFactory.getInstance("RSA")

        keyFactory.generatePublic(spec) as RSAPublicKey
    }

    private val algorithm: Algorithm = Algorithm.RSA256(publicKey, null)
    private const val issuer = "phoenix.io"
    private const val audience = "userinfoservice"

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun authenticate(credential: JWTCredential): Principal =
        UserPrincipal(
            credential.payload.subject,
            credential.payload.getClaim("username").asString()
        )
}
