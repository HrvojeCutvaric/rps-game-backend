package co.hrvoje.rpsgame.data.network.ws.api.models.games

import com.google.gson.annotations.SerializedName

data class JoinGameRequest(
    @SerializedName("username")
    val username: String,
)
