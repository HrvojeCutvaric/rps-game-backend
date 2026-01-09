package co.hrvoje.data.db.dao

import co.hrvoje.data.db.tables.Rounds
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RoundDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoundDAO>(Rounds)

    var game by GameDAO referencedOn Rounds.gameId
    var startedAt by Rounds.startedAt
}