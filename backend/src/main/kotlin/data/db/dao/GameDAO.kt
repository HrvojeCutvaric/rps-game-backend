package co.hrvoje.data.db.dao

import co.hrvoje.data.db.tables.Games
import co.hrvoje.domain.utils.GameState
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GameDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GameDAO>(Games)

    var createdAt by Games.createdAt

    var state: GameState
        get() = GameState.valueOf(Games.state.getValue(this, ::state))
        set(value) {
            Games.state.setValue(this, ::state, value.name)
        }
}
