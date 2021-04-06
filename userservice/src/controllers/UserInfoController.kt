package com.phoenix.userservice.controllers

import com.phoenix.userservice.dtos.UserInfoCreateDTO
import com.phoenix.userservice.extensions.safeReceive
import com.phoenix.userservice.extensions.userPrincipal
import com.phoenix.userservice.services.UserInfoService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.userInfo() {

    val userInfoService: UserInfoService by inject()

    authenticate {

        route("/api/v1/user-info") {

            get {
                val user = call.userPrincipal!!
                call.respond(HttpStatusCode.OK, userInfoService.getUserInfo(user))
            }

            // Convenience route for inserting data
            post {
                val user = call.userPrincipal!!
                val userInfoCreateDTO = call.safeReceive<UserInfoCreateDTO>()
                call.respond(HttpStatusCode.Created, userInfoService.createUserInfo(user, userInfoCreateDTO))
            }
        }
    }
}
