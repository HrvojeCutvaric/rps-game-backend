package co.hrvoje.rpsgame.data.network.ws.api.models.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GamePlayerDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("user")
    val user: UserDTO,
    @SerialName("game")
    val game: GameDTO,
    @SerialName("score")
    val score: Int,
    @SerialName("has_created_game")
    val hasCreateGame: Boolean,
)
