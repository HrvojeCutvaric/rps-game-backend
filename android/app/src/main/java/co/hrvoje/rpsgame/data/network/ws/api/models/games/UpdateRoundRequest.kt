package co.hrvoje.rpsgame.data.network.ws.api.models.games

import com.google.gson.annotations.SerializedName

data class UpdateRoundRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("move")
    val move: String,
)
