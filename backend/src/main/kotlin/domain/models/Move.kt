package co.hrvoje.domain.models

import co.hrvoje.domain.utils.MoveType
import kotlinx.serialization.Serializable

@Serializable
data class Move(
    val id: Int,
    val user: User,
    val round: Round,
    val choice: MoveType,
)
