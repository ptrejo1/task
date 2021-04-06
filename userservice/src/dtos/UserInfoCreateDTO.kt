package com.phoenix.userservice.dtos

data class UserInfoCreateDTO(
    val email: String,
    val firstName: String,
    val lastName: String
)
