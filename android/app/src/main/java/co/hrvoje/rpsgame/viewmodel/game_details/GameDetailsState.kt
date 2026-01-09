package co.hrvoje.rpsgame.viewmodel.game_details

import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.Round

data class GameDetailsState(
    val game: Game?,
    val rounds: List<Round>?,
    val errorResource: Int?,
)
