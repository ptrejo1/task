package com.phoenix.userservice.repositories

import com.phoenix.userservice.config.DatabaseFactory.dbQuery
import com.phoenix.userservice.dtos.UserInfoCreateDTO
import com.phoenix.userservice.models.UserInfo
import com.phoenix.userservice.models.UserPrincipal
import com.phoenix.userservice.models.UsersInfo
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

interface UserInfoRepository {

    companion object {

        fun create(): UserInfoRepository = UserInfoRepositoryImpl()
    }

    suspend fun getUserInfo(userId: String): UserInfo?

    /**
     * @throws RuntimeException if unique constraint violated
     */
    fun createUserInfo(user: UserPrincipal, userInfoCreateDTO: UserInfoCreateDTO): UserInfo
}

class UserInfoRepositoryImpl : UserInfoRepository {

    override suspend fun getUserInfo(userId: String): UserInfo? = dbQuery {
        UsersInfo.select { UsersInfo.userId eq userId }
            .map { toUserInfo(it) }
            .singleOrNull()
    }

    override fun createUserInfo(user: UserPrincipal, userInfoCreateDTO: UserInfoCreateDTO): UserInfo {
        val id = try {
            insert(user, userInfoCreateDTO)
        } catch (err: ExposedSQLException) {
            throw RuntimeException("Unique constraint violated")
        }

        return transaction { UsersInfo.select { UsersInfo.id eq id }.map { toUserInfo(it) }.first() }
    }

    /**
     * @throws ExposedSQLException if unique constraint violated
     */
    private fun insert(user: UserPrincipal, userInfoCreateDTO: UserInfoCreateDTO) = transaction {
        UsersInfo.insertAndGetId {
            it[userId] = user.userId
            it[username] = user.username
            it[email] = userInfoCreateDTO.email
            it[firstName] = userInfoCreateDTO.firstName
            it[lastName] = userInfoCreateDTO.lastName
        }
    }

    private fun toUserInfo(row: ResultRow): UserInfo =
        UserInfo(
            row[UsersInfo.id].value,
            row[UsersInfo.createdAt],
            row[UsersInfo.userId],
            row[UsersInfo.username],
            row[UsersInfo.email],
            row[UsersInfo.firstName],
            row[UsersInfo.lastName],
        )
}
