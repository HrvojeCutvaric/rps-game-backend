package co.hrvoje.data.db.dao

import co.hrvoje.data.db.tables.Moves
import co.hrvoje.domain.utils.MoveType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MoveDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MoveDAO>(Moves)

    var user by UserDAO referencedOn Moves.userId
    var round by RoundDAO referencedOn Moves.roundId
    var choice: MoveType
        get() = MoveType.valueOf(Moves.choice.getValue(this, ::choice))
        set(value) {
            Moves.choice.setValue(this, ::choice, value.name)
        }
}
