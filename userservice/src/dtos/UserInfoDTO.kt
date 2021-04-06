package com.phoenix.userservice.dtos

import com.phoenix.userservice.models.UserInfo

data class UserInfoDTO(
    val userId: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
) {

    companion object {

        fun create(userInfo: UserInfo): UserInfoDTO =
            UserInfoDTO(
                userInfo.userId,
                userInfo.username,
                userInfo.email,
                userInfo.firstName,
                userInfo.lastName
            )
    }
}
