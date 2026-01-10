package co.hrvoje.rpsgame.viewmodel.games

import co.hrvoje.rpsgame.domain.models.Game

sealed interface GamesAction {

    data class OnGameClicked(val game: Game) : GamesAction

    data object OnGameCreateClicked : GamesAction

    data object OnLogoutClicked : GamesAction
}
