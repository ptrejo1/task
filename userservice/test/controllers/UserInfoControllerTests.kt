package controllers

import com.phoenix.userservice.dtos.UserInfoCreateDTO
import com.phoenix.userservice.dtos.UserInfoDTO
import com.phoenix.userservice.models.UserPrincipal
import com.phoenix.userservice.services.UserInfoService
import io.ktor.http.*
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import testingutils.testApp
import io.ktor.server.testing.*
import testingutils.invalidSampleJwt
import testingutils.sampleJwt
import kotlin.test.Test
import kotlin.test.assertEquals

class UserInfoControllerTests : AutoCloseKoinTest() {

    private val testModule = module {
        single<UserInfoService> { MockUserInfoService() }
    }

    @Test fun testGetUserInfo() = testApp(testModule) {
        val response = handleRequest(HttpMethod.Get, "/api/v1/user-info") {
            addHeader("Authorization", "Bearer $sampleJwt")
        }.response
        assertEquals(HttpStatusCode.OK, response.status())
    }

    @Test fun testGetUserInfoUnauthorized() = testApp(testModule) {
        val response = handleRequest(HttpMethod.Get, "/api/v1/user-info").response
        assertEquals(HttpStatusCode.Unauthorized, response.status())
    }

    @Test fun testGetUserInfoInvalidJwt() = testApp(testModule) {
        val response = handleRequest(HttpMethod.Get, "/api/v1/user-info"){
            addHeader("Authorization", "Bearer $invalidSampleJwt")
        }.response
        assertEquals(HttpStatusCode.Unauthorized, response.status())
    }

    @Test fun testCreateUserInfo() = testApp(testModule) {
        val response = handleRequest(HttpMethod.Post, "/api/v1/user-info") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader("Authorization", "Bearer $sampleJwt")
            setBody("{\"email\": \"email\", \"firstName\": \"fn\", \"lastName\": \"ln\"}")
        }.response
        assertEquals(HttpStatusCode.Created, response.status())
    }

    @Test fun testCreateUserInfoUnauthorized() = testApp(testModule) {
        val response = handleRequest(HttpMethod.Post, "/api/v1/user-info") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("{\"email\": \"email\", \"firstName\": \"fn\", \"lastName\": \"ln\"}")
        }.response
        assertEquals(HttpStatusCode.Unauthorized, response.status())
    }
}

private class MockUserInfoService : UserInfoService() {

    override suspend fun getUserInfo(user: UserPrincipal): UserInfoDTO {
        return UserInfoDTO("abc", "foo", "email", "fn", "ln")
    }

    override fun createUserInfo(user: UserPrincipal, userInfoCreateDTO: UserInfoCreateDTO): UserInfoDTO {
        return UserInfoDTO("abc", "foo", "email", "fn", "ln")
    }
}
