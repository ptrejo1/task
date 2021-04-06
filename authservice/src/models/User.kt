package com.phoenix.authservice.models

import org.joda.time.DateTime

object Users : IntIdCreatedTable() {
    val userId = varchar("user_id", 64).uniqueIndex()
    val username = varchar("username", 128).uniqueIndex()
    val password = varchar("password", 128)
}

data class User(
    override val id: Int,
    override val createdAt: DateTime,
    val userId: String,
    val username: String,
    val password: String
) : IntIdCreatedModel
