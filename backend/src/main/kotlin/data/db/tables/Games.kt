package co.hrvoje.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object Games : IntIdTable("games") {
    val state = varchar("state", 50)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
}