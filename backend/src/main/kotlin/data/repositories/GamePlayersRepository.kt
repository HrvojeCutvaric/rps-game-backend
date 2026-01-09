package co.hrvoje.data.repositories

import co.hrvoje.domain.models.GamePlayer
import co.hrvoje.domain.models.User

interface GamePlayersRepository {

    suspend fun create(gamePlayer: GamePlayer): GamePlayer?

    suspend fun getUserGamePlayers(user: User): List<GamePlayer>
}