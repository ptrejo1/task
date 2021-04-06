package com.phoenix.userservice.extensions

import com.fasterxml.jackson.databind.JsonMappingException
import com.phoenix.userservice.api.BadRequestException
import com.phoenix.userservice.models.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.authentication
import io.ktor.request.*

/**
 * @throws BadRequestException if json serialization fails
 */
suspend inline fun <reified T : Any> ApplicationCall.safeReceive(): T {
    return try {
        receive()
    } catch (e: ContentTransformationException) {
        throw BadRequestException("Invalid body")
    } catch (e: JsonMappingException) {
        throw BadRequestException("Invalid body")
    }
}

val ApplicationCall.userPrincipal get() = authentication.principal<UserPrincipal>()
