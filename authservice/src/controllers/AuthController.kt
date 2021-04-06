package com.phoenix.authservice.controllers

import com.phoenix.authservice.dtos.UserDTO
import com.phoenix.authservice.extensions.safeReceive
import com.phoenix.authservice.services.AuthService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.auth() {

    val authService: AuthService by inject()

    route("/api/v1/auth") {

        post("/signup") {
            val userDto = call.safeReceive<UserDTO>()
            call.respond(HttpStatusCode.Created, authService.signUp(userDto))
        }

        post("/login") {
            val userDto = call.safeReceive<UserDTO>()
            call.respond(HttpStatusCode.OK, authService.login(userDto))
        }
    }
}
