package com.phoenix.authservice.repositories

import com.phoenix.authservice.config.DatabaseFactory.dbQuery
import com.phoenix.authservice.models.User
import com.phoenix.authservice.models.Users
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

interface UserRepository {

    companion object {

        fun create(): UserRepository = UserRepositoryImpl()
    }

    suspend fun getUser(username: String): User?

    /**
     * @throws RuntimeException if username taken
     */
    fun createUser(username: String, password: String): User
}

class UserRepositoryImpl : UserRepository {

    override suspend fun getUser(username: String): User? = dbQuery {
        Users.select { Users.username eq username }
            .map { toUser(it) }
            .singleOrNull()
    }

    override fun createUser(username: String, password: String): User {
        val id = try {
            insert(username, password)
        } catch (err: ExposedSQLException) {
            throw RuntimeException("Unique constraint violated")
        }

        return transaction { Users.select { Users.id eq id }.map { toUser(it) }.first() }
    }

    /**
     * @throws ExposedSQLException if unique username constraint violated
     */
    private fun insert(username: String, password: String): EntityID<Int> = transaction {
        Users.insertAndGetId {
            it[userId] = UUID.randomUUID().toString()
            it[Users.username] = username
            it[Users.password] = password
        }
    }

    private fun toUser(row: ResultRow): User =
        User(
            row[Users.id].value,
            row[Users.createdAt],
            row[Users.userId],
            row[Users.username],
            row[Users.password]
        )
}
