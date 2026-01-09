package co.hrvoje.rpsgame.data.network.services

import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.Round
import co.hrvoje.rpsgame.domain.models.User

interface GamesService {

    suspend fun getGames(): Result<List<Game>>

    suspend fun createGame(user: User): Result<Unit>

    suspend fun getGameRounds(gameId: Int): Result<List<Round>>
    suspend fun joinGame(gameId: Int, user: User): Result<Unit>
}
