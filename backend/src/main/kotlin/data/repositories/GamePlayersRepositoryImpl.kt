package co.hrvoje.data.repositories

import co.hrvoje.data.db.dao.GamePlayerDAO
import co.hrvoje.data.db.mappers.suspendTransaction
import co.hrvoje.data.db.mappers.toGameDAO
import co.hrvoje.data.db.mappers.toGamePlayer
import co.hrvoje.data.db.mappers.toUserDAO
import co.hrvoje.data.db.tables.GamePlayers
import co.hrvoje.domain.models.GamePlayer
import co.hrvoje.domain.models.User

class GamePlayersRepositoryImpl : GamePlayersRepository {

    override suspend fun create(gamePlayer: GamePlayer): GamePlayer? = suspendTransaction {
        try {
            GamePlayerDAO.new {
                this.game = gamePlayer.game.toGameDAO()
                this.user = gamePlayer.user.toUserDAO()
                this.hasCreatedGame = gamePlayer.hasCreatedGame
                this.score = gamePlayer.score
            }.toGamePlayer()
        } catch (error: Throwable) {
            println("Error while creating game player: ${error.message}")
            null
        }
    }

    override suspend fun getUserGamePlayers(user: User): List<GamePlayer> = suspendTransaction {
        GamePlayerDAO.find { GamePlayers.userId eq user.id }.map { it.toGamePlayer() }
    }
}