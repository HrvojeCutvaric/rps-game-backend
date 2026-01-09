package co.hrvoje.rpsgame.viewmodel.game_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.data.network.services.GamesService
import co.hrvoje.rpsgame.navigation.AppNavigator
import co.hrvoje.rpsgame.navigation.Route
import co.hrvoje.rpsgame.utils.CurrentUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val appNavigator: AppNavigator,
    private val currentUser: CurrentUser,
    private val gamesService: GamesService,
) : ViewModel() {

    private val gameId = (appNavigator.backStack.last() as Route.GameDetail).gameId

    private val _state = MutableStateFlow<GameDetailsState?>(null)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                gamesService.getGameRounds(gameId).fold(
                    onSuccess = {
                        _state.value = GameDetailsState(
                            game = it.first().game,
                            rounds = it,
                            errorResource = null,
                        )
                    },
                    onFailure = {
                        _state.value = GameDetailsState(
                            game = null,
                            rounds = null,
                            errorResource = R.string.generic_error_message,
                        )
                    }
                )
                delay(1000)
            }
        }
    }

    fun execute(action: GameDetailsAction) {
        when (action) {
            GameDetailsAction.OnBackClicked -> {
                appNavigator.navigateBack()
            }

            GameDetailsAction.OnJoinClicked -> {
                currentUser.user?.let { user ->
                    _state.value?.let { currentState ->
                        currentState.game?.let { game ->
                            viewModelScope.launch {
                                gamesService.joinGame(
                                    gameId = game.id,
                                    user = user,
                                )
                            }
                        }
                    }
                }
            }

            is GameDetailsAction.OnMoveClicked -> {
                // TODO: implement on move clicked
            }
        }
    }
}
