package co.hrvoje.rpsgame.data.network.ws.api

import co.hrvoje.rpsgame.data.network.ws.api.models.games.GameDTO
import co.hrvoje.rpsgame.data.network.ws.api.models.games.GamePlayerDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GamesAPI {

    @GET("/games")
    suspend fun getGames(
        @Query("state") state: String?,
    ): Response<List<GameDTO>>

    @GET("/games/game_players")
    suspend fun getUserGamePlayers(
        @Query("username") username: String,
        @Query("state") state: String?,
    ): Response<List<GamePlayerDTO>>

}
