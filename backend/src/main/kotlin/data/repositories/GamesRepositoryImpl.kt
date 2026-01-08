package co.hrvoje.data.repositories

import co.hrvoje.data.db.dao.GameDAO
import co.hrvoje.data.db.mappers.suspendTransaction
import co.hrvoje.data.db.mappers.toGame
import co.hrvoje.data.db.tables.Games
import co.hrvoje.domain.models.Game
import co.hrvoje.domain.utils.GameState
import java.time.Instant

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

    override suspend fun getGames(state: GameState?): List<Game> = suspendTransaction {
        return@suspendTransaction state?.let {
            GameDAO.find { (Games.state eq state.toString()) }.map { it.toGame() }
        } ?: run {
            GameDAO.all().map { it.toGame() }
        }
    }

    override suspend fun update(game: Game): Game? = suspendTransaction {
        try {
            val gameDAO = GameDAO.findById(game.id)
                ?: throw IllegalArgumentException("Game with id ${game.id} not found")

            gameDAO.apply {
                this.state = game.state
                this.createdAt = Instant.ofEpochMilli(game.createdAt)
            }.toGame()
        } catch (error: Throwable) {
            println("Error while updating game: ${error.message}")
            null
        }
    }

    override suspend fun getGameById(id: Int): Game = suspendTransaction {
        val gameDAO = GameDAO.findById(id)
            ?: throw IllegalArgumentException("Game with id $id not found")
        gameDAO.toGame()
    }
}