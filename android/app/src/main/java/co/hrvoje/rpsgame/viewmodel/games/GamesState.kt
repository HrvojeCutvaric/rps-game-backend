package co.hrvoje.rpsgame.viewmodel.games

import co.hrvoje.rpsgame.domain.models.Game

data class GamesState(
    val games: List<Game>,
    val errorResource: Int? = null,
    val gameInProgress: Game? = null,
)
