package co.hrvoje.rpsgame.data.network.ws

import co.hrvoje.rpsgame.data.network.services.GamesService
import co.hrvoje.rpsgame.data.network.ws.api.GamesAPI
import co.hrvoje.rpsgame.data.network.ws.api.mappers.toGame
import co.hrvoje.rpsgame.domain.models.Game
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

                    val floorMaps = body.map { it.toGame() }

                    return Result.success(floorMaps)
                }

                false -> {
                    return Result.failure(Throwable(message = "Failed to fetch floor map"))
                }
            }
        } catch (error: Throwable) {
            return Result.failure(error)
        }
    }
}
