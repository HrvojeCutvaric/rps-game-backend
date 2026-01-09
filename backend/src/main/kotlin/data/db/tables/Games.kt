package co.hrvoje.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object Games : IntIdTable("games") {
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
    val firstUserId = reference("first_user_id", Users)
    val secondUserId = reference("second_user_id", Users).nullable()
}