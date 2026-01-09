package co.hrvoje.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object Rounds : IntIdTable("rounds") {
    val gameId = reference("game_id", Games)
    val startedAt = timestamp("started_at").defaultExpression(CurrentTimestamp())
}