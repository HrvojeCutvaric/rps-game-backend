package co.hrvoje.rpsgame.data.network.ws.api.models.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String,
)
