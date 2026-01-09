package co.hrvoje.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Moves : IntIdTable("moves") {
    val userId = reference("user_id", Users)
    val roundId = reference("round_id", Rounds)
    val choice = varchar("choice", 50)
}