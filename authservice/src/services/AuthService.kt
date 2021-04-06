package com.phoenix.authservice.services

import com.phoenix.authservice.api.BadRequestException
import com.phoenix.authservice.api.UnauthorizedException
import com.phoenix.authservice.dtos.TokenDTO
import com.phoenix.authservice.dtos.UserDTO
import com.phoenix.authservice.interactors.JwtFactory
import com.phoenix.authservice.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt

open class AuthService(private val userRepository: UserRepository = UserRepository.create()) {

    /**
     * @throws BadRequestException if username is already taken
     */
    open fun signUp(userDTO: UserDTO): TokenDTO {
        if (!isValidPassword(userDTO.password))
            throw BadRequestException("Password should be between 8 and 64 characters")

        val hashed = BCrypt.hashpw(userDTO.password, BCrypt.gensalt())
        val user = try {
            userRepository.createUser(userDTO.username, hashed)
        } catch (err: RuntimeException) {
            throw BadRequestException("Username already taken.")
        }

        return TokenDTO(JwtFactory.createToken(user))
    }

    /**
     * @throws BadRequestException if username or password aren't correct
     */
    open suspend fun login(userDTO: UserDTO): TokenDTO {
        val user = userRepository.getUser(userDTO.username)
            ?.takeIf { BCrypt.checkpw(userDTO.password, it.password) }
            ?: run { throw UnauthorizedException()  }

        return TokenDTO(JwtFactory.createToken(user))
    }

    /**
     * Simple password validation
     */
    private fun isValidPassword(password: String): Boolean {
        return password.length in 8..64
    }
}
