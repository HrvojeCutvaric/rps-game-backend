package co.hrvoje.rpsgame.data.network.services

import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.utils.GameStatus

interface GamesService {

    suspend fun getGames(state: GameStatus?): Result<List<Game>>
}
