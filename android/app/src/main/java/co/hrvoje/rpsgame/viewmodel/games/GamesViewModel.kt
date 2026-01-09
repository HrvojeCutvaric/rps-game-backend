package co.hrvoje.rpsgame.viewmodel.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.data.network.services.GamesService
import co.hrvoje.rpsgame.domain.utils.GameStatus
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
    currentUser: CurrentUser,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val _state = MutableStateFlow<GamesState?>(null)
    val state = _state.asStateFlow()

    init {
        currentUser.user?.let {
            viewModelScope.launch {
                gamesService.getUserGamePlayers(
                    user = it,
                    status = GameStatus.IN_PROGRESS,
                ).fold(
                    onSuccess = { gamePlayers ->
                        val gameInProgress =
                            gamePlayers.firstOrNull { it.game.status == GameStatus.IN_PROGRESS }
                        gameInProgress?.let {
                            appNavigator.navigateTo(
                                route = Route.Login,
                                removeRoutes = listOf(Route.Games),
                            )
                            return@launch
                        }
                        while (true) {
                            gamesService.getGames(GameStatus.WAITING_FOR_PLAYERS).fold(
                                onSuccess = { games ->
                                    val userGames = gamePlayers.map { it.game }
                                    val filteredGames = games.minus(userGames)

                                    _state.update {
                                        it?.copy(
                                            games = filteredGames,
                                        ) ?: GamesState(
                                            games = filteredGames,
                                            errorResource = null,
                                        )
                                    }
                                },
                                onFailure = {
                                    _state.value = GamesState(
                                        games = emptyList(),
                                        errorResource = R.string.generic_error_message,
                                    )
                                }
                            )
                            delay(1000)
                        }
                    },
                    onFailure = {
                        _state.value = GamesState(
                            games = emptyList(),
                            errorResource = R.string.generic_error_message,
                        )
                    }
                )
            }
        }
    }

    fun execute(action: GamesAction) {
        when (action) {
            GamesAction.OnGameCreateClicked -> {
                // TODO: implement on game create clicked
            }

            is GamesAction.OnGamesJoinClicked -> {
                // TODO: implement on games join clicked
            }
        }
    }
}
