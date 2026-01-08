package co.hrvoje.data.repositories

import co.hrvoje.domain.models.GamePlayer

interface GamePlayersRepository {

    suspend fun create(gamePlayer: GamePlayer): GamePlayer?
}