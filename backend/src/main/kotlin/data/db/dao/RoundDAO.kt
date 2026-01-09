package co.hrvoje.data.db.dao

import co.hrvoje.data.db.tables.Rounds
import co.hrvoje.domain.utils.MoveType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RoundDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoundDAO>(Rounds)

    var game by GameDAO referencedOn Rounds.gameId
    var createdAt by Rounds.createdAt
    var firstUserMove: MoveType?
        get() = Rounds.firstUserMove
            .getValue(this, ::firstUserMove)
            ?.let { MoveType.valueOf(it) }
        set(value) {
            Rounds.firstUserMove
                .setValue(this, ::firstUserMove, value?.name)
        }
    var secondUserMove: MoveType?
        get() = Rounds.secondUserMove
            .getValue(this, ::secondUserMove)
            ?.let { MoveType.valueOf(it) }
        set(value) {
            Rounds.secondUserMove
                .setValue(this, ::secondUserMove, value?.name)
        }
}
