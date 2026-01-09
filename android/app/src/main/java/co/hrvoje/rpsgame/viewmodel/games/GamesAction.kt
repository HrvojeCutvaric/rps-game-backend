package co.hrvoje.rpsgame.viewmodel.games

sealed interface GamesAction {
    data class OnGamesJoinClicked(val gameId: Int) : GamesAction
    data object OnGameCreateClicked : GamesAction
}
