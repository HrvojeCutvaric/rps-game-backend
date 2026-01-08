package co.hrvoje.routing.models.games.create

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGameRequest(
    @SerialName("username") val username: String,
)
