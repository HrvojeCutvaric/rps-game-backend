package co.hrvoje.routing.models.join

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JoinGameRequest(
    @SerialName("username")
    val username: String,
)