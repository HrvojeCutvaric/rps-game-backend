package co.hrvoje.rpsgame.data.network.ws.api

import co.hrvoje.rpsgame.data.network.ws.api.models.games.CreateGameRequest
import co.hrvoje.rpsgame.data.network.ws.api.models.games.GameDTO
import co.hrvoje.rpsgame.data.network.ws.api.models.games.JoinGameRequest
import co.hrvoje.rpsgame.data.network.ws.api.models.games.RoundDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GamesAPI {

    @GET("/games")
    suspend fun getGames(): Response<List<GameDTO>>

    @POST("/games")
    suspend fun createGame(
        @Body createGameRequest: CreateGameRequest
    ): Response<GameDTO>

    @GET("/games/{gameId}/rounds")
    suspend fun getGameRounds(
        @Path("gameId") gameId: Int
    ): Response<List<RoundDTO>>

    @POST("/games/{gameId}/join")
    suspend fun joinGame(
        @Path("gameId") gameId: Int,
        @Body joinGameRequest: JoinGameRequest,
    ): Response<Unit>
}
