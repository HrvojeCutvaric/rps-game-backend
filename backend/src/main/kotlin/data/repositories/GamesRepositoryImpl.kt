package co.hrvoje.data.repositories

import co.hrvoje.data.db.dao.GameDAO
import co.hrvoje.data.db.mappers.suspendTransaction
import co.hrvoje.data.db.mappers.toGame
import co.hrvoje.data.db.mappers.toUserDAO
import co.hrvoje.domain.models.Game
import co.hrvoje.domain.models.User
import java.time.Instant

class GamesRepositoryImpl : GamesRepository {

    override suspend fun create(user: User): Game? = suspendTransaction {
        try {
            GameDAO.new {
                this.firstUser = user.toUserDAO()
            }.toGame()
        } catch (error: Throwable) {
            println("Error while creating game: ${error.message}")
            null
        }
    }

    override suspend fun getGames(): List<Game> = suspendTransaction {
        GameDAO.all().map { it.toGame() }
    }

    override suspend fun update(game: Game): Game? = suspendTransaction {
        try {
            val gameDAO = GameDAO.findById(game.id)
                ?: throw IllegalArgumentException("Game with id ${game.id} not found")

            gameDAO.apply {
                this.createdAt = Instant.ofEpochMilli(game.createdAt)
                this.firstUser = game.firstUser.toUserDAO()
                this.secondUser = game.secondUser?.toUserDAO()
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