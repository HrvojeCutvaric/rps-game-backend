package co.hrvoje.rpsgame.domain.models

import kotlinx.serialization.Serializable

data class Game(
    val id: Int,
    val createAt: Long,
    val firstUser: User,
    val secondUser: User?,
)
