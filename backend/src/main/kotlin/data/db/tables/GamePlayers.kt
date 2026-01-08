package co.hrvoje.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object GamePlayers : IntIdTable("game_players") {
    val userId = reference("user_id", Users)
    val gameId = reference("game_id", Games)
    val score = integer("score").default(0)
    val hasCreatedGame = bool("has_created_game").default(false)
}