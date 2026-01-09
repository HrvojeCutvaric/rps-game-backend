package co.hrvoje.rpsgame.viewmodel.game_details

import co.hrvoje.rpsgame.domain.utils.Move

sealed interface GameDetailsAction {

    data object OnBackClicked : GameDetailsAction

    data object OnJoinClicked : GameDetailsAction

    data class OnMoveClicked(val move: Move) : GameDetailsAction
}
