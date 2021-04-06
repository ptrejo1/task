package com.phoenix.userservice.models

import org.joda.time.DateTime

object UsersInfo : IntIdCreatedTable("users_info") {
    val userId = varchar("user_id", 64).uniqueIndex()
    val username = varchar("username", 128)
    val email = varchar("email", 256)
    val firstName = varchar("first_name", 128)
    val lastName = varchar("last_name", 128)
}

data class UserInfo(
    override val id: Int,
    override val createdAt: DateTime,
    val userId: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
) : IntIdCreatedModel
