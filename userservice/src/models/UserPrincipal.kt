package com.phoenix.userservice.models

import io.ktor.auth.*

data class UserPrincipal(
    val userId: String,
    val username: String
) : Principal
