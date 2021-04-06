package com.phoenix.userservice.services

import com.phoenix.userservice.api.NotFoundException
import com.phoenix.userservice.dtos.UserInfoCreateDTO
import com.phoenix.userservice.dtos.UserInfoDTO
import com.phoenix.userservice.models.UserPrincipal
import com.phoenix.userservice.repositories.UserInfoRepository

open class UserInfoService(private val userInfoRepository: UserInfoRepository = UserInfoRepository.create()) {

    /**
     * @throws NotFoundException if user info not found
     */
    open suspend fun getUserInfo(user: UserPrincipal): UserInfoDTO {
        val userInfo = userInfoRepository.getUserInfo(user.userId) ?: throw NotFoundException()
        return UserInfoDTO.create(userInfo)
    }

    open fun createUserInfo(user: UserPrincipal, userInfoCreateDTO: UserInfoCreateDTO): UserInfoDTO {
        val userInfo = userInfoRepository.createUserInfo(user, userInfoCreateDTO)
        return UserInfoDTO.create(userInfo)
    }
}
