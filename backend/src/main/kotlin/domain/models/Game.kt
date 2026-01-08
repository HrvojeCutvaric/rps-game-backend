package co.hrvoje.domain.models

import co.hrvoje.domain.utils.GameState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    @SerialName("id")
    val id: Int,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("state")
    val state: GameState,
)
