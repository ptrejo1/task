package com.phoenix.authservice.extensions

import com.fasterxml.jackson.databind.JsonMappingException
import com.phoenix.authservice.api.BadRequestException
import io.ktor.application.*
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
