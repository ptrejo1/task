package com.phoenix.authservice.api

import io.ktor.http.*

open class APIException(open val status: HttpStatusCode, message: String) : Exception(message) {

    fun serialize(): APIExceptionJson {
        val value = status.value
        return APIExceptionJson(value, message!!)
    }
}

data class APIExceptionJson(
    val status: Int,
    val message: String
)

class UnauthorizedException : APIException(HttpStatusCode.Unauthorized, "Invalid credentials.")

class BadRequestException(message: String = "Bad request") : APIException(HttpStatusCode.BadRequest, message)
