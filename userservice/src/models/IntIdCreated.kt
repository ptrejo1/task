package com.phoenix.userservice.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

open class IntIdCreatedTable(name: String = "") : IntIdTable(name) {
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
}

interface IntIdCreatedModel {
    val id: Int
    val createdAt: DateTime
}
