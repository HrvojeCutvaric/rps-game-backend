package co.hrvoje.routing.models.update_round

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRoundRequest(
    @SerialName("username")
    val username: String,
    @SerialName("move")
    val move: String,
)
