package co.hrvoje.data.repositories

import co.hrvoje.data.db.dao.GameDAO
import co.hrvoje.data.db.dao.RoundDAO
import co.hrvoje.data.db.mappers.suspendTransaction
import co.hrvoje.data.db.mappers.toRound
import co.hrvoje.domain.models.Round
import co.hrvoje.domain.utils.GameState

class RoundsRepositoryImpl : RoundsRepository {

    override suspend fun create(gameId: Int): Round? = suspendTransaction {
        try {
            val game = GameDAO.findById(gameId)
                ?: throw IllegalArgumentException("Game not found")

            if (game.state != GameState.IN_PROGRESS) {
                throw IllegalStateException("Game is not in progress")
            }

            RoundDAO.new {
                this.game = game
            }.toRound()
        } catch (error: Throwable) {
            println("Error while creating new round: ${error.message}")
            null
        }
    }

    override suspend fun getRoundById(roundId: Int): Round? = suspendTransaction {
        RoundDAO.findById(roundId)?.toRound()
    }
}