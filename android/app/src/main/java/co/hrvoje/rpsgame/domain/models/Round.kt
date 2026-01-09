package co.hrvoje.rpsgame.domain.models

import co.hrvoje.rpsgame.domain.utils.Move

data class Round(
    val id: Int,
    val game: Game,
    val createdAt: Long,
    val firstUserMove: Move?,
    val secondUserMove: Move?,
)
