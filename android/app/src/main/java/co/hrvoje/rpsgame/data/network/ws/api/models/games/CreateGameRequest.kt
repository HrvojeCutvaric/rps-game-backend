package co.hrvoje.rpsgame.data.network.ws.api.models.games

import com.google.gson.annotations.SerializedName

data class CreateGameRequest(
    @SerializedName("username")
    val username: String,
)
