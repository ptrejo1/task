package com.phoenix.authservice.config

import io.github.cdimascio.dotenv.DotEnvException
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

object Config {

    private val env: Dotenv? by lazy {
        try {
            dotenv()
        } catch (e: DotEnvException) {
            null
        }
    }

    private fun getEnvVar(envVar: String): String? {
        return System.getenv(envVar) ?: env?.get(envVar)
    }

    val dbHost: String by lazy {
        val envVar = getEnvVar("DB_HOST") ?:
            throw RuntimeException("Missing DB_HOST env var")
        envVar
    }

    val dbPort: String by lazy {
        val envVar = getEnvVar("DB_PORT") ?:
            throw RuntimeException("Missing DB_PORT env var")
        envVar
    }

    val dbName: String by lazy {
        val envVar = getEnvVar("DB_NAME") ?:
            throw RuntimeException("Missing DB_NAME env var")
        envVar
    }

    val dbUser: String by lazy {
        val envVar = getEnvVar("DB_USER") ?:
            throw RuntimeException("Missing DB_USER env var")
        envVar
    }

    val dbPassword: String by lazy {
        val envVar = getEnvVar("DB_PASSWORD") ?:
            throw RuntimeException("Missing DB_PASSWORD env var")
        envVar
    }

    val jwtPrivateKey: String by lazy {
        val envVar = getEnvVar("PRIVATE_KEY") ?:
        throw RuntimeException("Missing PRIVATE_KEY env var")
        envVar
    }
}
