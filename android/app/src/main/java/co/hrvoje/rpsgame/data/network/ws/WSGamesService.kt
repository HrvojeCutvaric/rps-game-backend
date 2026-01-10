package co.hrvoje.rpsgame.data.network.ws

import android.util.Log
import co.hrvoje.rpsgame.data.network.services.GamesService
import co.hrvoje.rpsgame.data.network.ws.api.GamesAPI
import co.hrvoje.rpsgame.data.network.ws.api.mappers.toGame
import co.hrvoje.rpsgame.data.network.ws.api.mappers.toRound
import co.hrvoje.rpsgame.data.network.ws.api.models.games.CreateGameRequest
import co.hrvoje.rpsgame.data.network.ws.api.models.games.JoinGameRequest
import co.hrvoje.rpsgame.data.network.ws.api.models.games.UpdateRoundRequest
import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.Round
import co.hrvoje.rpsgame.domain.models.User
import co.hrvoje.rpsgame.domain.utils.Move

class WSGamesService(
    private val gamesAPI: GamesAPI,
) : GamesService {

    override suspend fun getGames(): Result<List<Game>> {
        try {
            val result = gamesAPI.getGames()

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
            Log.e("WSGamesService", "getGames: ", error)
            return Result.failure(error)
        }
    }

    override suspend fun createGame(user: User): Result<Unit> {
        try {
            val result =
                gamesAPI.createGame(createGameRequest = CreateGameRequest(username = user.username))

            return when (result.isSuccessful) {
                true -> Result.success(Unit)
                false -> Result.failure(Throwable(message = "Failed to create a game"))
            }
        } catch (error: Throwable) {
            Log.e("WSGamesService", "createGame: ", error)
            return Result.failure(error)
        }
    }

    override suspend fun getGameRounds(gameId: Int): Result<List<Round>> {
        try {
            val result = gamesAPI.getGameRounds(gameId)

            return when (result.isSuccessful) {
                true -> {
                    val body = result.body() ?: return Result.failure(Throwable("Body is null"))

                    val data = body.map { it.toRound() }

                    Result.success(data)
                }

                false -> {
                    Result.failure(Throwable(message = "Failed to fetch rounds"))
                }
            }
        } catch (error: Throwable) {
            Log.e("WSGamesService", "getGameRounds: ", error)
            return Result.failure(error)
        }
    }

    override suspend fun joinGame(gameId: Int, user: User): Result<Unit> {
        try {
            val result =
                gamesAPI.joinGame(
                    gameId = gameId,
                    joinGameRequest = JoinGameRequest
                        (username = user.username)
                )

            return when (result.isSuccessful) {
                true -> Result.success(Unit)
                false -> Result.failure(Throwable(message = "Failed to join this game"))
            }
        } catch (error: Throwable) {
            Log.e("WSGamesService", "joinGame: ", error)
            return Result.failure(error)
        }
    }

    override suspend fun updateRound(
        gameId: Int,
        roundId: Int,
        user: User,
        move: Move
    ): Result<Unit> {
        try {
            val result =
                gamesAPI.updateRound(
                    gameId = gameId,
                    roundId = roundId,
                    updateRoundRequest = UpdateRoundRequest(
                        username = user.username,
                        move = move.name,
                    )
                )

            return when (result.isSuccessful) {
                true -> Result.success(Unit)
                false -> Result.failure(Throwable(message = "Failed to update round"))
            }
        } catch (error: Throwable) {
            Log.e("WSGamesService", "updateRound: ", error)
            return Result.failure(error)
        }
    }
}
