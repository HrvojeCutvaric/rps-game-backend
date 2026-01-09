package co.hrvoje.routing.models.create_move

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMoveRequest(
    @SerialName("username")
    val username: String,
    @SerialName("choice")
    val choice: String,
)
