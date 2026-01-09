package co.hrvoje.rpsgame.data.network.ws

import co.hrvoje.rpsgame.data.network.services.GamesService
import co.hrvoje.rpsgame.data.network.ws.api.GamesAPI
import co.hrvoje.rpsgame.data.network.ws.api.mappers.toGame
import co.hrvoje.rpsgame.data.network.ws.api.mappers.toGamePlayer
import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.GamePlayer
import co.hrvoje.rpsgame.domain.models.User
import co.hrvoje.rpsgame.domain.utils.GameStatus

class WSGamesService(
    private val gamesAPI: GamesAPI,
) : GamesService {

    override suspend fun getGames(state: GameStatus?): Result<List<Game>> {
        try {
            val result = gamesAPI.getGames(state = state?.name)

            when (result.isSuccessful) {
                true -> {
                    val body = result.body() ?: return Result.failure(Throwable("Body is null"))

                    val data = body.map { it.toGame() }

                    return Result.success(data)
                }

                false -> {
                    return Result.failure(Throwable(message = "Failed to fetch games"))
                }
            }
        } catch (error: Throwable) {
            return Result.failure(error)
        }
    }

    override suspend fun getUserGamePlayers(
        user: User,
        status: GameStatus
    ): Result<List<GamePlayer>> {
        try {
            val result = gamesAPI.getUserGamePlayers(
                username = user.username,
                state = status.name,
            )

            when (result.isSuccessful) {
                true -> {
                    val body = result.body() ?: return Result.failure(Throwable("Body is null"))

                    val data = body.map { it.toGamePlayer() }

                    return Result.success(data)
                }

                false -> {
                    return Result.failure(Throwable(message = "Failed to fetch game players"))
                }
            }
        } catch (error: Throwable) {
            return Result.failure(error)
        }
    }
}
