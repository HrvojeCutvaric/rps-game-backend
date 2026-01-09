package co.hrvoje.rpsgame.data.network.ws.api

import co.hrvoje.rpsgame.data.network.ws.api.models.games.CreateGameRequest
import co.hrvoje.rpsgame.data.network.ws.api.models.games.GameDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GamesAPI {

    @GET("/games")
    suspend fun getGames(): Response<List<GameDTO>>

    @POST("/games")
    suspend fun createGame(
        @Body createGameRequest: CreateGameRequest
    ): Response<GameDTO>
}
