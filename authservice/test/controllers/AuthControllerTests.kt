package controllers

import com.phoenix.authservice.dtos.TokenDTO
import com.phoenix.authservice.dtos.UserDTO
import com.phoenix.authservice.services.AuthService
import io.ktor.http.*
import io.ktor.server.testing.*
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import testingutils.testApp
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthControllerTests : AutoCloseKoinTest() {

    private val testModule = module {
        single<AuthService> { MockAuthService() }
    }

    @Test fun testSignUp() = testApp(testModule) {
        val response = handleRequest(HttpMethod.Post, "/api/v1/auth/signup") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("{\"username\": \"foo\", \"password\": \"bar\"}")
        }.response
        assertEquals(HttpStatusCode.Created, response.status())
    }

    @Test fun testLogin() = testApp(testModule) {
        val response = handleRequest(HttpMethod.Post, "/api/v1/auth/login") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("{\"username\": \"foo\", \"password\": \"bar\"}")
        }.response
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

private class MockAuthService : AuthService() {

    override fun signUp(userDTO: UserDTO): TokenDTO {
        return TokenDTO("abc123")
    }

    override suspend fun login(userDTO: UserDTO): TokenDTO {
        return TokenDTO("abc123")
    }
}
