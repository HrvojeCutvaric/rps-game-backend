package co.hrvoje.data.db.dao

import co.hrvoje.data.db.tables.Games
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GameDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GameDAO>(Games)

    var createdAt by Games.createdAt
    var firstUser by UserDAO referencedOn Games.firstUserId
    var secondUser by UserDAO optionalReferencedOn Games.secondUserId
}
