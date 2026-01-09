package co.hrvoje.rpsgame.data.network.ws.api.models.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("state")
    val state: String,
)
