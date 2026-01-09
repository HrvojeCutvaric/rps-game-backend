package co.hrvoje.routing.models.current_game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentGameRequest(
    @SerialName("username")
    val username: String,
)
