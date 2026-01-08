package co.hrvoje.data.db.dao

import co.hrvoje.data.db.tables.GamePlayers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GamePlayerDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GamePlayerDAO>(GamePlayers)

    var user by UserDAO referencedOn GamePlayers.userId
    var game by GameDAO referencedOn GamePlayers.gameId
    var score by GamePlayers.score
    var hasCreatedGame by GamePlayers.hasCreatedGame
}