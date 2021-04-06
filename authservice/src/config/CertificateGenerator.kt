package com.phoenix.authservice.config

import io.ktor.network.tls.certificates.*
import java.io.File

object CertificateGenerator {

    @JvmStatic
    fun main(args: Array<String>) {
        val jksFile = File("build/temporary.jks").apply {
            parentFile.mkdirs()
        }

        if (!jksFile.exists()) generateCertificate(jksFile)
    }
}
