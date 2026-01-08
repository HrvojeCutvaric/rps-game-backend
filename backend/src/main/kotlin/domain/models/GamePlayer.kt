package co.hrvoje.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class GamePlayer(
    val user: User,
    val game: Game,
    val score: Int,
    val hasCreatedGamme: Boolean,
)
