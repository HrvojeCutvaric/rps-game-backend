package co.hrvoje.domain.models

import co.hrvoje.domain.utils.MoveType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Round(
    @SerialName("id")
    val id: Int,
    @SerialName("game")
    val game: Game,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("first_user_move")
    val firstUserMove: MoveType?,
    @SerialName("second_user_move")
    val secondUserMove: MoveType?,
)

