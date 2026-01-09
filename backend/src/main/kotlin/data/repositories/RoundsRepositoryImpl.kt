package co.hrvoje.data.repositories

import co.hrvoje.data.db.dao.GameDAO
import co.hrvoje.data.db.dao.RoundDAO
import co.hrvoje.data.db.mappers.suspendTransaction
import co.hrvoje.data.db.mappers.toRound
import co.hrvoje.data.db.tables.Rounds
import co.hrvoje.domain.models.Round

class RoundsRepositoryImpl : RoundsRepository {

    override suspend fun create(gameId: Int): Round? = suspendTransaction {
        try {
            val game = GameDAO.findById(gameId)
                ?: throw IllegalArgumentException("Game not found")

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

    override suspend fun getRoundsByGameId(gameId: Int): List<Round> = suspendTransaction {
        try {
            RoundDAO.find { Rounds.gameId eq gameId }.map { it.toRound() }
        } catch (e: Throwable) {
            println("Error while getting rounds by game: ${e.message}")
            emptyList()
        }
    }
}