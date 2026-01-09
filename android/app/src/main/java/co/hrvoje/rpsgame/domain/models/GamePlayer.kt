package co.hrvoje.rpsgame.domain.models

data class GamePlayer(
    val id: Int,
    val user: User,
    val game: Game,
    val score: Int,
    val hasCreateGame: Boolean,
)
