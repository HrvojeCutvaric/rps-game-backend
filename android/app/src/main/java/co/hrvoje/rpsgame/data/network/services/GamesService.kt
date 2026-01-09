package co.hrvoje.rpsgame.data.network.services

import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.GamePlayer
import co.hrvoje.rpsgame.domain.models.User
import co.hrvoje.rpsgame.domain.utils.GameStatus

interface GamesService {

    suspend fun getGames(state: GameStatus?): Result<List<Game>>

    suspend fun getUserGamePlayers(user: User, status: GameStatus): Result<List<GamePlayer>>
}
