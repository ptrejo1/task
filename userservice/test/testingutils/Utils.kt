package testingutils

import com.phoenix.userservice.module
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.testing.*
import org.koin.core.module.Module

inline fun <reified T: Any> String.fromJson(): T = jacksonObjectMapper().readValue(this, T::class.java)

val TestApplicationResponse.contentOrEmpty: String
    get() = this.content ?: ""

fun testApp(testModule: Module, callback: TestApplicationEngine.() -> Unit) {
    withTestApplication({ module(testModule, false) }) { callback() }
}

const val sampleJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJ1c2VyaW5mb3NlcnZpY2UiLCJzdWIiOiJmNjI2ZDM5MC1mNGUxLTRhM2YtOTg5MS0zNGJhOWI5ODJiZDMiLCJpc3MiOiJwaG9lbml4LmlvIiwiZXhwIjoxNjE4ODc5MzcyLCJ1c2VybmFtZSI6ImZvb2JhciJ9.ZbCtkRtWiQH-SuuCPanS6lHrVGZ_hJVL7QxDA54bPfT1na4SZ5OgI6eAtWCzipzfMuFx7axBKTAzbP-cofJuGCq_mm68vA-JzmIkspkPJLoEoE1j08prGRS_b5BfS40_GT8AjItjT9DN0HfMVZwyXL-VC1KKnbPCBqHaGbIKMdPINJm9B4IDktCoe4dCpzUVk0UNzZMvTwxCYiTlBJzjih53uz4fMjShs37In9rCfB7g58kvb83gMRmc-FpkJieZUgGhc_DwprJfTcKvi8uLARQ0ztf3oqNCCes_asHpz7YoMXIFwE98IKmJFqX8goxW-HqS2hBDchmmCU3gBPXAWA"
const val invalidSampleJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJ1c2VyaW5mb3NlcnZpY2UiLCJzdWIiOiJmNjI2ZDM5MC1mNGUxLTRhM2YtOTg5MS0zNGJhOWI5ODJiZDMiLCJpc3MiOiJwaG9lbml4LmlvIiwiZXhwIjoxNjE4ODc5MzcyLCJ1c2VybmFtZSI6ImZvb2JhciJ9.ZbCtkRtWiQH-SuuCPanS6lHrVGZ_hJVL7QxDA54bPfT1na4SZ5OgI6eAtWCzipzfMuFx7axBKTAzbP-cofJuGCq_mm68vA-JzmIkspkPJLoEoE1j08prGRS_b5BfS40_GT8AjItjT9DN0HfMVZwyXL-VC1KKnbPCBqHaGbIKMdPINJm9B4IDktCoe4dCpzUVk0UNzZMvTwxCYiTlBJzjih53uz4fMjShs37In9rCfB7g58kvb83gMRmc-FpkJieZUgGhc_DwprJfTcKvi8uLARQ0ztf3oqNCCes_asHpz7YoMXIFwE98IKmJFqX8goxW-HqS2hBDchmmCU3gINVALID"

