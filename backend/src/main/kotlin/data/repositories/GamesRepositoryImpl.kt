package co.hrvoje.data.repositories

import co.hrvoje.data.db.dao.GameDAO
import co.hrvoje.data.db.mappers.suspendTransaction
import co.hrvoje.data.db.mappers.toGame
import co.hrvoje.domain.models.Game
import co.hrvoje.domain.utils.GameState

class GamesRepositoryImpl : GamesRepository {

    override suspend fun create(): Game? = suspendTransaction {
        try {
            GameDAO.new {
                this.state = GameState.WAITING_FOR_PLAYERS
            }.toGame()
        } catch (error: Throwable) {
            println("Error while creating game: ${error.message}")
            null
        }
    }
}