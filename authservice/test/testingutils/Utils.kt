package testingutils

import com.phoenix.authservice.module
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.testing.*
import org.koin.core.module.Module

inline fun <reified T: Any> String.fromJson(): T = jacksonObjectMapper().readValue(this, T::class.java)

val TestApplicationResponse.contentOrEmpty: String
    get() = this.content ?: ""

fun testApp(testModule: Module, callback: TestApplicationEngine.() -> Unit) {
    withTestApplication({ module(testModule, false) }) { callback() }
}
