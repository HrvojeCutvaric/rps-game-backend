package co.hrvoje.rpsgame.viewmodel.games

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GamesViewModel(
    private val gamesService: GamesService,
    private val currentUser: CurrentUser,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val _state = MutableStateFlow<GamesState?>(null)
    val state = _state.asStateFlow()

    init {
        currentUser.user?.let { currentUser ->
            viewModelScope.launch {
                while (true) {
                    gamesService.getGames().fold(
                        onSuccess = { games ->
                            _state.update {
                                it?.copy(
                                    games = games,
                                    errorResource = null,
                                ) ?: GamesState(
                                    games = games,
                                    errorResource = null,
                                    currentUser = currentUser
                                )
                            }
                        },
                        onFailure = {
                            _state.value = GamesState(
                                games = emptyList(),
                                errorResource = R.string.generic_error_message,
                                currentUser = currentUser
                            )
                        }
                    )
                    delay(1000)
                }
            }
        }
    }

    fun execute(action: GamesAction) {
        when (action) {
            GamesAction.OnGameCreateClicked -> {

                currentUser.user?.let { user ->
                    viewModelScope.launch {
                        gamesService.createGame(user = user)
                    }
                }
            }

            is GamesAction.OnGameClicked -> {
                appNavigator.navigateTo(route = Route.GameDetails(action.game))
            }
        }
    }
}
