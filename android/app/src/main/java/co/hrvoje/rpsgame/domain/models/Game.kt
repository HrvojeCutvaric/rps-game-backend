package co.hrvoje.rpsgame.domain.models

import co.hrvoje.rpsgame.domain.utils.GameStatus

data class Game(
    val id: Int,
    val createAt: Long,
    val firstUser: User,
    val secondUser: User?,
)
