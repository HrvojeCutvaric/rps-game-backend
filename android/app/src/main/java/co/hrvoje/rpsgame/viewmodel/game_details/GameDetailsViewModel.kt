package co.hrvoje.rpsgame.viewmodel.game_details

import androidx.lifecycle.ViewModel
import co.hrvoje.rpsgame.navigation.AppNavigator
import co.hrvoje.rpsgame.navigation.Route

class GameDetailsViewModel(
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val gameId = (appNavigator.backStack.last() as Route.GameDetail).gameId

    init {

    }

    suspend fun loadRounds(gameId: String) {
        // dohvacam sve ronde za dati gameId
    }
}
