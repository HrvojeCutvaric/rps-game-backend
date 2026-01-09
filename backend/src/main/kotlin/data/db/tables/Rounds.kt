package co.hrvoje.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object Rounds : IntIdTable("rounds") {
    val gameId = reference("game_id", Games)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
    val firstUserMove = varchar("first_user_move", 50).nullable()
    val secondUserMove = varchar("second_user_move", 50).nullable()
}