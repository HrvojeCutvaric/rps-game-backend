package co.hrvoje.rpsgame.viewmodel.games

import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.User

data class GamesState(
    val games: List<Game>,
    val errorResource: Int? = null,
    val currentUser: User,
)
