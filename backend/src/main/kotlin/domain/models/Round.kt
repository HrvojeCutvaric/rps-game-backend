package co.hrvoje.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Round(
    @SerialName("id")
    val id: Int,
    @SerialName("game")
    val game: Game,
    @SerialName("stated_at")
    val startedAt: Long
)

