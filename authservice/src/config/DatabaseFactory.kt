package com.phoenix.authservice.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {

    private val jdbcUrl = "jdbc:postgresql://${Config.dbHost}:${Config.dbPort}/${Config.dbName}"

    fun init() {
        Database.connect(hikari())
        migrate()
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = jdbcUrl
        config.username = Config.dbUser
        config.password = Config.dbPassword
        config.maximumPoolSize = 1
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.addDataSourceProperty("reWriteBatchedInserts", "true")
        config.validate()

        return HikariDataSource(config)
    }

    private fun migrate() {
        val flyway = Flyway.configure()
            .dataSource(jdbcUrl, Config.dbUser, Config.dbPassword)
            .locations("filesystem:resources/db/migrations")
            .load()
        flyway.migrate()
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction { block() }
}
