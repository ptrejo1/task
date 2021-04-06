package com.phoenix.authservice

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.SerializationFeature
import com.phoenix.authservice.api.APIException
import com.phoenix.authservice.config.DatabaseFactory
import com.phoenix.authservice.config.serviceModule
import com.phoenix.authservice.controllers.auth
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(module: Module = serviceModule(), isDbEnabled: Boolean = true) {
    install(Koin) { modules(module) }

    install(CallLogging) { level = Level.INFO }

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        }
    }

    install(StatusPages) {
        exception<APIException> { cause ->
            log.error(cause.message)
            call.respond(cause.status, cause.serialize())
        }
        exception<Throwable> { cause ->
            log.error(cause.stackTraceToString())
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    if (isDbEnabled) DatabaseFactory.init()

    install(Routing) {
        auth()
    }
}
